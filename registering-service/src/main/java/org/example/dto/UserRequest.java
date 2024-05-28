package org.example.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.regex.Pattern;

@Data
public class UserRequest implements Serializable {
    private String username;
    private String email;

    private String password;
    private String role;
    private String imageBytesBase64;


    public boolean isValid(){
        return username != null && email != null && password != null && role != null && this.isRoleValid();
    }




    public boolean isRoleValid(){
        if (this.role != null)
            return this.role.equals("USER") || this.role.equals("SINGER");
        return false;
    }

    private boolean isPasswordValid(){final String PASSWORD_REGEX = "^.{8}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        return pattern.matcher(password).matches();}


}
