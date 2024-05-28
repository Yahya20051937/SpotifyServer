package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.dto.UserRequest;
import org.example.model.Response;
import org.example.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationService service;

    @PostMapping("/spotify/api/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody UserRequest userRequest) throws JsonProcessingException, InterruptedException {
        Response response = service.authenticateUser(userRequest);
        return response.toResponseEntity();
    }

    @GetMapping("/spotify/api/authenticate/role/{username}")
    public String getRole(@PathVariable("username") String username){return service.getUserRole(username).getName();
    }
    @GetMapping("/spotify/api/authenticate/id/{username}")
    public String getId(@PathVariable("username") String username){
        return service.getUserId(username);
    }


}
