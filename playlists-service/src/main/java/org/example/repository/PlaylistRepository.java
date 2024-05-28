package org.example.repository;

import org.example.entity.Playlist;
import org.springframework.data.repository.CrudRepository;

public interface PlaylistRepository extends CrudRepository<Playlist, String> {
    
}
