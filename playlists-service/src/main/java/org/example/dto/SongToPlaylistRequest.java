package org.example.dto;

import lombok.Data;

@Data
public class SongToPlaylistRequest {
    private String playlistId;
    private String songId;
}
