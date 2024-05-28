package org.example.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.util.Map;

@Data
@AllArgsConstructor
public class Response {
    private String content;
    private int statusCode;

    public ResponseEntity<String> toResponseEntity(){
        return new ResponseEntity<>(content, HttpStatusCode.valueOf(statusCode));
    }

    public Response(ClientResponse clientResponse) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> responseMap = mapper.readValue(clientResponse.bodyToMono(String.class).block(), Map.class);
        System.out.println(responseMap);
        this.content = responseMap.get("errorMessage");
        this.statusCode = clientResponse.statusCode().value();
    }
}
