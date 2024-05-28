package org.example.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.nio.channels.MulticastChannel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SongRequest {
    private String name;
    private int duration;
    private int bitRate;
    private MultipartFile image;
    private MultipartFile audio;
}
