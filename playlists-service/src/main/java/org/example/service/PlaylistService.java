package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.dto.PlaylistRequest;
import org.example.dto.PlaylistResponse;
import org.example.dto.SongToPlaylistRequest;
import org.example.entity.Playlist;
import org.example.entity.SongPlaylist;
import org.example.entity.UserPlaylist;
import org.example.model.Song;
import org.example.model.User;
import org.example.repository.PlaylistRepository;
import org.example.repository.SongPlaylistRepository;
import org.example.repository.UserPlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlaylistService{
    @Autowired
    PlaylistRepository playlistRepository;

    @Autowired
    UserPlaylistRepository userPlaylistRepository;

    @Autowired
    SongPlaylistRepository songPlaylistRepository;

    @Autowired
    @Qualifier("intern")
    WebClient.Builder webClient;


    public String createPlaylist(PlaylistRequest request){
        Playlist playlist = new Playlist(UUID.randomUUID().toString(), request.getName());
        playlistRepository.save(playlist);
        UserPlaylist userPlaylistRelationShip = new UserPlaylist(request.getUsername(), playlist.getId());
        userPlaylistRepository.save(userPlaylistRelationShip);
        return playlist.getId();
    }

    public void addSongToPlaylist(SongToPlaylistRequest request){
        if (songPlaylistRepository.findByPlaylistIdAndSongId(request.getPlaylistId(), request.getSongId()) == null) {
            SongPlaylist songPlaylistRelationship = new SongPlaylist(request.getPlaylistId(), request.getSongId());
            songPlaylistRepository.save(songPlaylistRelationship);
        }
    }

    public void removeSongFromPlaylist(SongToPlaylistRequest request){
        SongPlaylist songPlaylist = songPlaylistRepository.findByPlaylistIdAndSongId(request.getPlaylistId(), request.getSongId());
        if (songPlaylist != null)
            songPlaylistRepository.delete(songPlaylist);
    }

    public String getPlaylists(User user, HttpServletRequest request) throws IOException {
        List<UserPlaylist> userPlaylists = userPlaylistRepository.findByUsername(user.getUsername());
        List<PlaylistResponse> playlistResponses = this.getPlaylistsResponses(userPlaylists, request);
        return this.playlistResponsesToJsonArray(playlistResponses);
    }

    public String playlistResponsesToJsonArray(List<PlaylistResponse> playlistResponses) throws IOException {
        StringBuilder builder = new StringBuilder("[");
        for (PlaylistResponse playlistResponse : playlistResponses){
            builder.append(playlistResponse.getJson());
            builder.append(",");
        }
        return builder.substring(0, builder.length() - 1) + "]";
    }

    public List<PlaylistResponse> getPlaylistsResponses(List<UserPlaylist> userPlaylists, HttpServletRequest request) throws JsonProcessingException {
        List<PlaylistResponse> playlists = new ArrayList<>();
        for (UserPlaylist userPlaylist: userPlaylists) {
            Playlist playlist = playlistRepository.findById(userPlaylist.getPlaylist_id()).get();
            playlists.add(new PlaylistResponse(playlist, this.getPlaylistSongs(playlist, request)));
        }
        return playlists;
    }

    public List<Song> getPlaylistSongs(Playlist playlist, HttpServletRequest request) throws JsonProcessingException {
        List<SongPlaylist> playlistSongs = songPlaylistRepository.findByPlaylistId(playlist.getId());
        List<Song> songs = new ArrayList<>();
        System.out.println(request.getHeader("Authorization"));
        for (SongPlaylist songPlaylist:playlistSongs){
            String songJson = webClient.build()
                    .get()
                    .uri("http://search-service/spotify/api/search/internal/" + songPlaylist.getSongId())
                    .header("Authorization", request.getHeader("Authorization"))
                    .retrieve()
                    .bodyToMono(
                            String.class
                    ).block();
            songs.add(new Song(songJson));
        }
        return songs;
    }




}










