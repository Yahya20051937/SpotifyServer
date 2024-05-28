package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.SingerRequest;
import org.example.dto.SongRequest;
import org.example.model.User;
import org.example.service.UploadService;
import org.example.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {
    @Autowired
    UploadService service;

    @Autowired
    UserService userService;

    @PostMapping("/spotify/api/upload/song")
    public ResponseEntity<String> uploadSong(@RequestParam("audio") MultipartFile audioFile, @RequestParam("image") MultipartFile image, @RequestParam("bitRate") int bitRate, @RequestParam("duration") int duration, @RequestParam("name") String name) throws IOException {
        SongRequest songRequest = SongRequest.builder().audio(audioFile)
                .image(image)
                .bitRate(bitRate)
                .duration(duration)
                .name(name)
                .build();
        if (userService.getAuthenticatedSinger().hasRole("SINGER")){
            service.uploadSong(songRequest);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }


    @PostMapping("/spotify/api/upload/singer")
    public ResponseEntity<String> uploadSinger(@RequestBody SingerRequest singerRequest) throws IOException {
        if (userService.getAuthenticatedSinger().hasRole("SINGER")) {
            service.saveSinger(singerRequest);
            return new ResponseEntity<>("", HttpStatusCode.valueOf(200));
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(401));
    }


}
