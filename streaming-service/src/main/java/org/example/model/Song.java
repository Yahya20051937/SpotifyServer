package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

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
    private int nbStreams;
    private int nbLikes;

    public Song(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(json,Map.class);
        this.id = (String) map.get("id");
        this.name = (String) map.get("name");
        this.path = (String) map.get("path");
        this.nbBytes = (int) map.get("nbBytes");
        this.bitRate = (int) map.get("bitRate");
        this.duration = (int) map.get("duration");
    }
}
