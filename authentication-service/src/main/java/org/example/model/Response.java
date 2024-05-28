package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class Response {
    private String content;
    private int statusCode;

    public ResponseEntity<String> toResponseEntity(){
        return new ResponseEntity<>(content, HttpStatusCode.valueOf(statusCode));
    }
}

