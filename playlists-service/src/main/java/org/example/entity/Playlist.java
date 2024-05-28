package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Playlist {
    @Id
    private String id;
    private String name;

    public Playlist(String name){
        this.name = name;
    }

    public Playlist(String id, String name ){
        this.id = id;
        this.name = name;
    }
}
