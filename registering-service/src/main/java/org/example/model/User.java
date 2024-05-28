package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.UserRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{
    private String id;
    private String username;
    private  String password;
    private String roleName;
    private  String email;

    public User(String id, UserRequest userRequest){
        this.id = id;
        this.username = userRequest.getUsername();
        this.password = userRequest.getPassword();
        this.email = userRequest.getEmail();
        this.roleName = userRequest.getRole();
    }




}
