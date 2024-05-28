package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import org.example.dto.PlaylistRequest;
import org.example.dto.SongToPlaylistRequest;
import org.example.model.User;
import org.example.service.PlaylistService;
import org.example.service.UserService;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class PlaylistController {
    @Autowired
    PlaylistService service;

    @Autowired
    UserService userService;


    @PostMapping("/spotify/api/playlists/create")
    public Response createPlaylist(@RequestBody PlaylistRequest playlistRequest){
        service.createPlaylist(playlistRequest);
        return Response.ok("").build();
    }

    @PostMapping("/spotify/api/playlists/add")
    public Response addSong(@RequestBody SongToPlaylistRequest request){
        service.addSongToPlaylist(request);
        return Response.ok().build();
    }

    @PostMapping("/spotify/api/playlists/remove")
    public Response removeSong(@RequestBody SongToPlaylistRequest request){
        service.removeSongFromPlaylist(request);
        return Response.ok().build();
    }

    @GetMapping("/spotify/api/playlists/get")
    public String get(HttpServletRequest request) throws IOException {
        User user = new User(SecurityContextHolder.getContext().getAuthentication(), userService);
        return service.getPlaylists(user, request);
    }


}

























