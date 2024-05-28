package org.example.model;

import lombok.Data;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Data
public class JWT {
    private String tokenType;
    private String accessToken;

    public JWT(String token){
        this.tokenType = token.split(" ")[0];
        this.accessToken = token.split(" ")[1];
    }

    public Jwt decode(JwtDecoder decoder){
        return decoder.decode(this.accessToken);
    }
}
