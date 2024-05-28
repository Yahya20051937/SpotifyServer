package org.example.config;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {
    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
                .serverUrl("http://localhost:8180/")
                .realm("spotify")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId("spotify-client")
                .clientSecret("piiQrTiv5uZVR215IiueOfpSo9tWFibO")
                .build();
    }
}
