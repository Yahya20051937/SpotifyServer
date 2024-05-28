package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class JWT {
    private final String accessToken;
    private final String tokenType;
    private final String scope;

    public JWT(String json, String role) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> jwtMap = objectMapper.readValue(json,Map.class);
        this.accessToken = jwtMap.get("access_token");
        this.tokenType = jwtMap.get("token_type");
        this.scope = role;
    }


    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = writer.writeValueAsString(this);
        System.out.println(json);
        return json;
    }


}

