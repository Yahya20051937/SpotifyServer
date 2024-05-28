package org.example.repository;

import org.example.entity.UserPlaylist;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserPlaylistRepository extends CrudRepository<UserPlaylist, Long> {
    List<UserPlaylist> findByUsername(String username);
}
