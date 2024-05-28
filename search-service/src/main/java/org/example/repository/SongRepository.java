package org.example.repository;
import org.example.document.Song;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SongRepository extends MongoRepository<Song, String> {
    List<Song> findBySingerId(String singerId);
}
