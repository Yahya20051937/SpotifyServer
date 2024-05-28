package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.model.User;
import org.example.service.SearchService;
import org.example.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;


@RestController
public class SearchController {
    @Autowired
    SearchService service;

    @Autowired
    UserService userService;

    public User getAuthenticatedUser(){
        return new User(SecurityContextHolder.getContext().getAuthentication(), userService);
    }

    @GetMapping("/spotify/api/search/{searchString}")
    public String search(@PathVariable String searchString) throws IOException {
        System.out.println("external search ... ");
        String resultJson = service.getFoundElements(searchString);
        return resultJson;
    }

    @GetMapping("/spotify/api/search/internal/{id}")
    public String internalSearch(@PathVariable String id) throws IOException {
        System.out.println("internal search : " + id );
        return service.getSong(id);
    }

    @GetMapping("/spotify/api/search/internal/singer/check/presence/{username}")
    public boolean internalCheck(@PathVariable String username){
        return service.isUsernameOfASinger(username);
    }

}
