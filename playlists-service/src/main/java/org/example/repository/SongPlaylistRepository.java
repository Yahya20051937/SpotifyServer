package org.example.repository;

import org.example.entity.SongPlaylist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SongPlaylistRepository extends CrudRepository<SongPlaylist, Long> {
    List<SongPlaylist> findByPlaylistId(String playlistId);
    SongPlaylist findByPlaylistIdAndSongId(String playlistId, String songId);
}
