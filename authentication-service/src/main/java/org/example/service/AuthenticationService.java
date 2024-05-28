package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Synchronized;
import org.example.model.Response;
import org.example.dto.UserRequest;
import org.example.model.JWT;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthenticationService {
    @Autowired
    @Qualifier("extern")
    WebClient.Builder externWebClientBuilder;

    @Autowired
    @Qualifier("intern")
    WebClient.Builder internWebClientBuilder;

    @Autowired
    Keycloak keycloak;
    private static final String clientId = "804095dc-3b8f-477c-b95c-ac21ce8c731a";

    private static final List<String> applicationRoles = List.of("USER", "SINGER");


    public Response authenticateUser(UserRequest userRequest) throws JsonProcessingException, InterruptedException {
        final String tokenEndpoint = "http://keycloak:8180/realms/spotify/protocol/openid-connect/token";
        final String contentType = "application/x-www-form-urlencoded";
        ClientResponse clientResponse = externWebClientBuilder.build()
                .post()
                .uri(tokenEndpoint)
                .header("Content-Type", contentType)
                .body(this.getRequestBody(userRequest))
                .exchange()
                .block();
        assert clientResponse != null;
        if (clientResponse.statusCode().value() == 401)
            return new Response("Invalid Credentials", 401);
        return new Response(new JWT(clientResponse.bodyToMono(String.class).block(), this.getUserRole(userRequest.getUsername()).getName()).toJson(), 200);
    }

    public RoleRepresentation getUserRole(String username){
        UsersResource usersResource = keycloak.realm("spotify").users();
        List<RoleRepresentation> roles = usersResource.get(this.getUserId(username)).roles().clientLevel(clientId).listAll();
        for (RoleRepresentation roleRepresentation : roles)
            for (String appRole : applicationRoles)
                if (appRole.equals(roleRepresentation.getName()))
                    return roleRepresentation;
        return null;
    }

    public String getUserId(String username){
        try {
            return keycloak.realm("spotify").
                    users().
                    searchByUsername(username, true).
                    get(0).getId();
        }
        catch (Exception e) {
            return null;
        }

    }



    private BodyInserters.FormInserter<String> getRequestBody(UserRequest userRequest){
        final String clientId = "spotify-client";
        final String clientSecret = "piiQrTiv5uZVR215IiueOfpSo9tWFibO";
        final String grantType = "password";
        return BodyInserters.fromFormData("grant_type", grantType)
                .with("client_id", clientId)
                .with("client_secret", clientSecret)
                .with("username", userRequest.getUsername())
                .with("password", userRequest.getPassword());


    }




}
