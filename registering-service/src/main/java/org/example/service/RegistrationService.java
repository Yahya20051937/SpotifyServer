package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.*;

import org.example.model.User;
import org.example.model.JWT;
import org.example.model.Role;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletionStage;

@Service
public class RegistrationService {
    private static final String clientId = "804095dc-3b8f-477c-b95c-ac21ce8c731a";

    WebClient.Builder externWebClientBuilder;
    WebClient.Builder internWebClientBuilder;
    @Autowired
    public Keycloak keycloak;
    private JWT jwt;

    @Autowired
    public RegistrationService(@Qualifier("extern") WebClient.Builder externWebClientBuilder, @Qualifier("intern") WebClient.Builder internWebClientBuilder) throws JsonProcessingException {

        this.externWebClientBuilder = externWebClientBuilder;
        this.internWebClientBuilder = internWebClientBuilder;
        this.setJwt(this.getJwtRequestBody());
    }

    public ResponseEntity<String> registrationAlgorithm(@RequestBody UserRequest userRequest) throws IOException, ServletException {
        System.out.println("Registering  ...");
        if (userRequest.isValid()) {
            Response registrationResponse = this.registerUser(userRequest);
            if (registrationResponse.getStatusCode() == 200) {

                System.out.println("Assigning ...");
                String userId = this.getUserId(userRequest);
                User user = new User(userId, userRequest);
                this.sendAssigningRequest(this.getRoleInfo(userRequest.getRole()), user);

                System.out.println("Authenticating ...");
                Response authenticationResponse = this.sendAuthenticationRequest(userRequest);
                if (authenticationResponse.getStatusCode() == 200) {
                    JWT userJwt = new JWT(authenticationResponse.getContent());



                    if (userRequest.getRole().equals("SINGER")) {
                        System.out.println(userRequest.getImageBytesBase64());
                        this.sendUploadSingerRequest(userRequest, userJwt);
                    }
                    else {
                        System.out.println("Creating playlist ...");
                        this.sendCreateLikedSongsPlaylistRequest(userRequest, userJwt);} // if not we send a request to create an empty liked songs playlists}
                    return authenticationResponse.toResponseEntity();
                }
                return registrationResponse.toResponseEntity();
            }
            return registrationResponse.toResponseEntity();
        }
        return new ResponseEntity<>("Invalid request", HttpStatus.valueOf(400)) ;
    }


    public Response registerUser(UserRequest userRequest) throws JsonProcessingException{
        if (this.jwt.isExpired())
            this.setJwt(this.getJwtRequestBody());
        ClientResponse registrationResponse = this.sendKeycloakRequest(userRequest);
        System.out.println(registrationResponse.statusCode());
        if (registrationResponse.statusCode().value() == 201) {
            return new Response("", 200);
        }
        System.out.println("Conflict");
        return new Response("Conflict", 409);
    }

    private ClientResponse sendKeycloakRequest(UserRequest userRequest) throws JsonProcessingException {
        final String contentType = "application/json";
        String registrationEndpoint = "http://keycloak:8180/admin/realms/spotify/users";
        return externWebClientBuilder.build()
        .post()
        .uri(registrationEndpoint)
        .header("Content-Type", contentType).header("Authorization", this.jwt.getTokenType() + " " + this.jwt.getAccessToken())
        .bodyValue(new RegistrationRequestBody(userRequest).toJson())
        .exchange().block();

    }


    public Response sendAuthenticationRequest(UserRequest userRequest) throws JsonProcessingException {
        final String endpoint = "http://authentication-service/spotify/api/authenticate";
        final String contentType = "application/json";
        ClientResponse response = internWebClientBuilder.build()
                .post()
                .uri(endpoint)
                .header("Content-Type", contentType)
                .bodyValue(new AuthenticationRequestBody(userRequest).toJson())
                .exchange().block();
        assert response != null;
        if (response.statusCode().value() != 200) {
            System.out.println("Authentication Drop Down");
            throw new RuntimeException();
        }
        return new Response(response.bodyToMono(String.class).block(), response.statusCode().value());
    }

    private String getUserId(UserRequest userRequest) throws JsonProcessingException {
        String endpoint = "http://keycloak:8180/admin/realms/spotify/users";
        endpoint += "?email=" + userRequest.getEmail();
        String response = externWebClientBuilder.build()
                .get()
                .uri(endpoint)
                .header("Authorization", this.jwt.getTokenType() + " " + this.jwt.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return this.retrieveUserIdFromResponse(response);
    }

    private String retrieveUserIdFromResponse(String response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<LinkedHashMap<String, Object>> list = mapper.readValue(response, List.class);
        return (String) list.get(0).get("id");
    }

    private Role getRoleInfo(String roleName) throws JsonProcessingException {
        final String endpoint = "http://keycloak:8180/admin/realms/spotify/clients/"+ clientId +"/roles/" + roleName;
        String responseJson = externWebClientBuilder.build()
                .get()
                .uri(endpoint)
                .header("Authorization", this.jwt.getTokenType() + " " + this.jwt.getAccessToken())
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return new Role(responseJson);
    }

    public HttpStatusCode sendAssigningRequest(Role role, User user){
        UsersResource usersResource = keycloak.realm("spotify").
                users();
        ClientResource clientResource = keycloak.realm("spotify").clients().get(clientId);
        RoleRepresentation roleRepresentation = this.getRoleFromClientRoles(clientResource,  role);
        assert roleRepresentation != null;
        usersResource.get(user.getId()).roles().clientLevel(clientId).add(List.of(roleRepresentation));
        List<RoleRepresentation> roles = usersResource.get(user.getId()).roles().clientLevel(clientId).listAll();
        for (RoleRepresentation r:roles)
            if (role.getName().equals(r.getName()))
                return HttpStatusCode.valueOf(204);

        System.out.println("Role Keycloak Drop Down");
        throw new RuntimeException();
    }
    public JWT getJWT(BodyInserters.FormInserter<String> body) throws JsonProcessingException{
        final String contentType = "application/x-www-form-urlencoded";
        String tokenEndpoint = "http://keycloak:8180/realms/spotify/protocol/openid-connect/token";
        String jsonJwt = externWebClientBuilder.build()
                .post()
                .uri(tokenEndpoint)
                .header("Content-Type", contentType)
                .body(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return new JWT(jsonJwt);
    }
    public void setJwt(BodyInserters.FormInserter<String> body) throws JsonProcessingException {
        this.jwt = this.getJWT(body);
    }

    public BodyInserters.FormInserter<String> getJwtRequestBody(){
        String clientId = "spotify-client";
        String clientSecret = "piiQrTiv5uZVR215IiueOfpSo9tWFibO";
        String grantType = "client_credentials";
        return BodyInserters.fromFormData("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("grant_type", grantType);

    }


    public Response sendUploadSingerRequest(UserRequest userRequest, JWT userJwt) throws JsonProcessingException {
        final String endpoint = "http://upload-service/spotify/api/upload/singer";
        final String contentType = "application/json";
        String id = this.getUserId(userRequest);
        String requestJson = new SingerRequestBody(id, userRequest.getUsername(), userRequest.getImageBytesBase64()).toJson();
            ClientResponse response = internWebClientBuilder.build()
                        .post()
                        .uri(endpoint)
                        .header("Content-Type", contentType)
                        .header("Authorization", userJwt.getTokenType() + " " + userJwt.getAccessToken())
                        .bodyValue(requestJson)
                        .exchange().block();
        assert response != null;
        if (response.statusCode().value() != 200)
                throw new RuntimeException();

            return new Response("Singer uploaded successfully", 200);
    }


    private void sendCreateLikedSongsPlaylistRequest(UserRequest userRequest, JWT userJwt){
        final String endpoint = "http://playlists-service/spotify/api/playlists/create";
        final String contentType = "application/json";
        ClientResponse response = internWebClientBuilder.build()
                .post()
                .uri(endpoint)
                .header("Content-Type", contentType)
                .header("Authorization", userJwt.getTokenType() + " " + userJwt.getAccessToken())
                .bodyValue(new PlaylistRequest("Liked Songs", userRequest.getUsername()))
                .exchange().block();
        if (response.statusCode().value() != 200) {
            System.out.println("Playlist Drop down");
            throw new RuntimeException();
        }

    }

    private RoleRepresentation getRoleFromClientRoles(ClientResource clientResource, Role role){
        for (RoleRepresentation r: clientResource.roles().list())
            if (r.getId().equals(role.getId()))
                return r;
        return null;
    }


}
