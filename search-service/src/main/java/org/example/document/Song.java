package org.example.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.SpotifyObject;
import org.example.service.SearchService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Document("song")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @MongoId
    private String id;
    private String name;
    private String singerId;
    private String path;
    private int nbBytes;
    private int bitRate;
    private int duration;
    private int nbStreams;
    private int nbLikes;

    public String getJsonInfo(SearchService service) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("type", SpotifyObject.Type.song);
        map.put("name", this.name);
        map.put("duration", this.duration);
        map.put("nbBytes", this.nbBytes);
        map.put("bitRate", this.bitRate);
        Singer singer = service.getSingerById(this.singerId);
        map.put("singerName", singer.getName());
        map.put("singerId", singer.getId());
        map.put("image_bytes", service.getImageBytes(this.id));
        map.put("singer_image_bytes", service.getImageBytes(this.singerId));
        map.put("nbLikes", this.nbLikes);
        map.put("nbStreams", this.nbStreams);
        return new ObjectMapper().writeValueAsString(map);
    }


}
