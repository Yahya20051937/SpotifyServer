package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Data
public class Chunk {
    private long index;
    private String base64Bytes;

    public Chunk(byte[] bytes, int index){
        this.base64Bytes = Base64.getEncoder().encodeToString(bytes);
        this.index = index;
    }

    public Chunk(String base64Bytes, int index){
        this.base64Bytes = base64Bytes;
        this.index = index;
    }

    public String ToString(){return this.base64Bytes;}







}
