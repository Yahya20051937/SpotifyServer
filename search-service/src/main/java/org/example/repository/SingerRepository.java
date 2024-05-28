package org.example.repository;

import org.example.document.Singer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SingerRepository extends MongoRepository<Singer, String> {
    public Singer findByName(String name);
}
