package org.example.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.Data;
import org.example.document.Singer;
import org.example.document.Song;
import org.example.service.SearchService;

import java.util.List;

@Data
public class SpotifyObject {

    private String name;
    private String id;

    private List<Identifier> identifiers;
    private Type type;

    public SpotifyObject(Song song, SearchService service){
        this.name = song.getName();
        this.id = song.getId();
        this.type = Type.song;
        this.identifiers = service.getSongIdentifiers(song);

    }

    public SpotifyObject(Singer singer, SearchService service){
        this.name = singer.getName();
        this.id = singer.getId();
        this.type = Type.singer;
        this.identifiers = service.getSingerIdentifiers(singer);
    }

    public enum Type {singer, song}


}