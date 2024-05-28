package org.example.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
public class AuthenticationRequestBody {
    private String username;
    private String password;
    private String role;




    public AuthenticationRequestBody(UserRequest userRequest){
        this.username = userRequest.getUsername();
        this.password = userRequest.getPassword();
        this.role = userRequest.getRole();
    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(this);
    }
}