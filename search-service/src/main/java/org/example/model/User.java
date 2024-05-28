package org.example.model;

import lombok.Data;
import org.example.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class User {
    private String username;
    private String email;
    private String id;
    private String role;
    public User(Authentication authentication, UserService userService) {
        Jwt decodedJwt = (Jwt) authentication.getPrincipal();
        this.username = decodedJwt.getClaimAsString("preferred_username");
        this.email = decodedJwt.getClaimAsString("email");
        this.id = userService.getUserId(this.username);
        this.role = userService.getUserRole(this.username);
    }
}
