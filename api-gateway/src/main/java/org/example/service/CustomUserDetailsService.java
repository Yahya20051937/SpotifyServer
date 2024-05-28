package org.example.service;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    Keycloak keycloak;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserRepresentation userRepresentation = keycloak.realm("spotify").users().searchByUsername(username, true).get(0);
        System.out.println(username);
        return (UserDetails) userRepresentation;
    }
}
