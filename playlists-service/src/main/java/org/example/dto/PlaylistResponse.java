package org.example.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.example.entity.Playlist;
import org.example.model.Song;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PlaylistResponse {
    private String id;
    private String name;
    private List<Song> songs;

    public PlaylistResponse(Playlist playlist, List<Song> songs){
        this.id = playlist.getId();
        this.name = playlist.getName();
        this.songs = songs;
    }
    public String getSongsJsonArray() throws IOException {
        StringBuilder builder = new StringBuilder("[");
        for (Song song:this.songs) {
            builder.append(song.getJson());
            builder.append(",");
        }
        return builder.substring(0, builder.length() - 1) + "]";

    }

    public String getJson() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("name", this.name);
        map.put("songs", this.getSongsJsonArray());
        return new ObjectMapper().writeValueAsString(map);
    }

}
