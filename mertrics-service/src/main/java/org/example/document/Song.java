package org.example.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
    private int duration;
    private int bitRate;
    private int nbBytes;
    private int nbStreams;
    private int nbLikes;
}
