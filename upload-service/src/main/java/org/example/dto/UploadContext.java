package org.example.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadContext {
    private MultipartFile audioFile;
    private String name;
}
