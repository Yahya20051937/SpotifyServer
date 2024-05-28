package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.model.Chunk;
import org.example.model.Song;
import org.example.model.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;

@Service
public class StreamingService {

    @Autowired
    WebClient.Builder webClient;
    static final int CHUNK_SIZE = 64;
    private Flux<String> getAudioChunks(byte[] audioBytes){
        String base64Bytes = Base64.getEncoder().encodeToString(audioBytes);
        int dividedSize = base64Bytes.length() / CHUNK_SIZE;
        return Flux.range(0, dividedSize)
                .map(i -> {
                    int index1 = CHUNK_SIZE * i;
                    int index2 = CHUNK_SIZE * (i + 1);
                    return new Chunk(base64Bytes.substring(index1, index2), i).ToString();
                });
    }


    private Song getSong(String id, String accessToken) throws JsonProcessingException {
        String songJson = webClient.build()
                .get()
                .uri("http://search-service/spotify/api/search/internal/" + id)
                .header("Authorization", accessToken)
                .retrieve()
                .bodyToMono(
                        String.class
                ).block();
        return new Song(songJson);
    }


    @CircuitBreaker(name = "search", fallbackMethod = "searchFallBackMethod")
    public Flux<String> loadAudio(String id, Time startTime, String accessToken) throws IOException {
        System.out.println("New stream, start time : " + startTime);
        Path path = Path.of("/audios-datacenter/" + id + ".wav");
        byte[] audioBytes = Files.readAllBytes(path);
        Song song = this.getSong(id, accessToken);
        int bytesStartingIndex = this.getBytesStartingIndex(startTime, song);
        return this.getAudioChunks(Arrays.copyOfRange(audioBytes, bytesStartingIndex, audioBytes.length - 1));
    }

    public Object searchFallBackMethod(Throwable throwable){
        System.out.println("Fall back mechanism");
        return "Error";

    }

    private int getBytesStartingIndex(Time startTime, Song song){
        return (song.getBitRate() * startTime.getSecondsValue()) / 8;
    }


}
















