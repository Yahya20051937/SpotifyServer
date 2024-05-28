package org.example.document;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.model.SpotifyObject;
import org.example.service.SearchService;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Document("singer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Singer {
    @MongoId
    private String id;
    private String name;

    public String getJsonInfo(SearchService service) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("type", SpotifyObject.Type.singer);
        map.put("name", this.name);
        map.put("id", this.id);
        map.put("singer_image_bytes", service.getImageBytes(this.id));
        return new ObjectMapper().writeValueAsString(map);
    }
}
