package org.example.contoller;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import org.example.model.Time;
import org.example.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/spotify/api/stream/{id}/{startTime}")
public class StreamingController {

    @Autowired
    StreamingService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> stream(@PathVariable("id") String id, @PathVariable("startTime") int startTime, HttpServletRequest request) throws IOException {
        Flux<String> bytesFlux = service.loadAudio(id, new Time(startTime) ,request.getHeader("Authorization"));
        return bytesFlux;
    }



}
