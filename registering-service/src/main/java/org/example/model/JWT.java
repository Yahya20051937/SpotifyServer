package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

import java.util.Map;
import java.util.Optional;

@Data
public class JWT {
    private final String accessToken;
    private final String tokenType;
    private final String scope;
    private int expires_in;
    private long generated_in = System.currentTimeMillis();

    public JWT(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jwtMap = objectMapper.readValue(json, Map.class);

        this.accessToken = (String) Optional.ofNullable(jwtMap.get("access_token")).orElse(jwtMap.get("accessToken"));
        this.tokenType = (String) Optional.ofNullable(jwtMap.get("token_type")).orElse(jwtMap.get("tokenType"));
        this.scope = (String) jwtMap.get("scope");
        try {
            this.expires_in = (int) Optional.ofNullable(jwtMap.get("expires_in")).orElse(jwtMap.get("expiresIn"));
        } catch (NullPointerException ignored){}

    }

    public String toJson() throws JsonProcessingException {
        ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return writer.writeValueAsString(this);
    }

    public boolean isExpired(){
        return System.currentTimeMillis() >= this.generated_in + this.expires_in;
    }
}
