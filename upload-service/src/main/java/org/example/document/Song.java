package org.example.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.SongRequest;
import org.example.service.UploadService;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.nio.file.Files;
import java.nio.file.Path;

@Document("song")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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


    public Song(SongRequest songRequest, String singerUsername, UploadService service, String songId){
        this.id = songId;
        this.name = songRequest.getName();
        this.singerId = service.getSingerId(singerUsername);
        this.duration = songRequest.getDuration();
        this.path = "C:/spotify-datacenter/songs/" + this.id + ".wav";
        this.bitRate = songRequest.getBitRate();
        this.nbBytes = (this.bitRate * this.duration) / 8;
        this.nbStreams = 0;
        this.nbLikes = 0;
    }

}
