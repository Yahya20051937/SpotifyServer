package org.example.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.example.document.Singer;
import org.example.document.Song;
import org.example.dto.SingerRequest;
import org.example.dto.SongRequest;
import org.example.repository.SingerRepository;
import org.example.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class UploadService {

    @Autowired
    SongRepository songRepository;

    @Autowired
    SingerRepository singerRepository;

    @Autowired
    UserService userService;


    public void saveAudioFile(SongRequest songRequest, String songId) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("/audios-datacenter/" + songId + ".wav");
        byte[] audioBytes = songRequest.getAudio().getBytes();
        fileOutputStream.write(audioBytes);
        fileOutputStream.close();
    }


    public void saveSong(SongRequest songRequest, String singerUsername, String songId){
        Song song = new Song(songRequest, singerUsername, this, songId);
        songRepository.save(song);
    }

    public void saveImageFile(SongRequest songRequest, String songId) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream("/images-datacenter/" +  songId + ".png");
        byte[] imageBytes = songRequest.getImage().getBytes();
        fileOutputStream.write(imageBytes);
        fileOutputStream.close();
    }

    public void uploadSong(SongRequest songRequest) throws IOException {
        String songId = UUID.randomUUID().toString();
        this.saveAudioFile(songRequest, songId);
        this.saveImageFile(songRequest, songId);
        this.saveSong(songRequest, userService.getAuthenticatedSinger().getUsername(), songId);
    }

    public void saveSinger(SingerRequest singerRequest) throws IOException {
        Singer singer = new Singer(singerRequest);
        FileOutputStream fileOutputStream = new FileOutputStream("/images-datacenter/" + singer.getId() + ".png");
        byte[] imageBytes = Base64.getDecoder().decode(singerRequest.getImageBytesBase64());
        fileOutputStream.write(imageBytes);
        fileOutputStream.close();
        singerRepository.save(singer);
    }

    public String getSingerId(String singerName){
        List<Singer> singers = singerRepository.findAll();
        for (Singer singer:singers)
            if (singer.getName().equalsIgnoreCase(singerName))
                return singer.getId();
        return null;
    }
}
