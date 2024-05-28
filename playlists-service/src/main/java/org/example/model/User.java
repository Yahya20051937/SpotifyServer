package org.example.model;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.Data;
import org.example.service.PlaylistService;
import org.example.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class User {
    private String username;
    private String email;
    private String id;
    private String role;
    public User(Authentication authentication, UserService service) {
        Jwt decodedJwt = (Jwt) authentication.getPrincipal();
        this.username = decodedJwt.getClaimAsString("preferred_username");
        this.email = decodedJwt.getClaimAsString("email");
        this.id = service.getUserId(this.username);
        this.role = service.getUserRole(this.username);
    }
}