package org.example.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://keycloak:8180/")
                .realm("spotify")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("spotify-client")
                .clientSecret("piiQrTiv5uZVR215IiueOfpSo9tWFibO")
                .build();
    }

    @Bean
    CommandLineRunner commandLineRunner(){
        System.out.println("Test");
        return args -> {};
    }
}
