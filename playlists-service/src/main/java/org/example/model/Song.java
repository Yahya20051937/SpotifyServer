package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
public class Song {
    private String id;
    private String name;
    private String singerId;
    private String path;
    private int nbBytes;
    private int bitRate;
    private int duration;
    private String singerName;
    private String imageBytes;
    private String singerImageBytes;
    private int nbStreams;
    private int nbLikes;

    public Song(String json) throws JsonProcessingException {
        Map<String, Object> map = new ObjectMapper().readValue(json, Map.class);
        this.id = (String) map.get("id");
        this.name = (String) map.get("name");
        this.singerId = (String) map.get("singerId");
        this.path = (String) map.get("path");
        this.nbBytes = (int) map.get("nbBytes");
        this.bitRate = (int) map.get("bitRate");
        this.duration = (int) map.get("duration");
        this.singerName = (String) map.get("singerName");
        this.imageBytes = (String) map.get("image_bytes");
        this.singerImageBytes = (String) map.get("singer_image_bytes");
        this.nbLikes = (int) map.get("nbLikes");
        this.nbStreams = (int) map.get("nbStreams");
    }

    public String getJson() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("type", "song");
        map.put("name", this.name);
        map.put("duration", this.duration);
        map.put("nbBytes", this.nbBytes);
        map.put("bitRate", this.bitRate);
        map.put("singerName", this.singerName);
        map.put("singerId", this.singerId);
        map.put("image_bytes", this.getImageBytes());
        map.put("singer_image_bytes", this.getSingerImageBytes());
        map.put("nbLikes",  this.nbLikes);
        map.put("nbStreams", this.nbStreams);
        return new ObjectMapper().writeValueAsString(map).replace("{", "(").replace("}", ")");
    }
}
