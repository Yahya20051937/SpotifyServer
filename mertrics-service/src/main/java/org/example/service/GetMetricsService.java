package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.example.document.Song;
import org.example.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class GetMetricsService {
    @Autowired
    SongRepository repository;

    public Response getSongLikes(String songId) throws JsonProcessingException {
        Optional<Song> songOptional = repository.findById(songId);
        if (songOptional.isPresent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", songId);
            map.put("likes" , songOptional.get().getNbLikes());
            return Response.ok(new ObjectMapper().writer().writeValueAsString(map)).build();
        }
        return Response.noContent().build();
    }

    public Response getSongStreams(String songId) throws JsonProcessingException {
        Optional<Song> songOptional = repository.findById(songId);
        if (songOptional.isPresent()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", songId);
            map.put("streams" , songOptional.get().getNbStreams());
            return Response.ok(new ObjectMapper().writer().writeValueAsString(map)).build();
        }
        return Response.noContent().build();
    }
}
