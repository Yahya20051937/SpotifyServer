package org.example.service;

import org.example.document.Song;
import org.example.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateMetricsService {
    @Autowired
    SongRepository songRepository;

    public void modifyLikes(String songId, int incrementingValue){
        Optional<Song> songOptional = songRepository.findById(songId);
        if (songOptional.isPresent()){
            Song song = songOptional.get();
            if (song.getNbLikes() + incrementingValue >= 0) {
                song.setNbLikes(song.getNbLikes() + incrementingValue);
                songRepository.save(song);
            }
        }
    }

    public void incrementStreams(String songId){
        Optional<Song> songOptional = songRepository.findById(songId);
        if (songOptional.isPresent()){
            Song song = songOptional.get();
            song.setNbStreams(song.getNbStreams() + 1);
            songRepository.save(song);
        }
    }
}
