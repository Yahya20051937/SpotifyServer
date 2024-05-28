package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class UserService {
    @Autowired
    @Qualifier("intern")

    WebClient.Builder webClient;
    public String getUserRole(String username){
        return webClient.build()
                .get()
                .uri("http://authentication-service/spotify/api/authenticate/role/" + username)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String getUserId(String username){
        return webClient.build()
                .get()
                .uri("http://authentication-service/spotify/api/authenticate/id/" + username)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
