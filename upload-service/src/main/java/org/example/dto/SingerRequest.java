package org.example.dto;

import lombok.Data;

@Data
public class SingerRequest {
    private String id;
    private String name;
    private String imageBytesBase64;

}
