package org.example.model;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class JWT {
    private String tokenType;
    private String accessToken;

    public JWT(String token){
        System.out.println(token);
        this.tokenType = token.split(" ")[0];
        this.accessToken = token.split(" ")[1];
    }



    public Jwt decode(JwtDecoder decoder){
        return decoder.decode(this.accessToken);
    }
}
