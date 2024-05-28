package org.example.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.ServletRequest;
import jakarta.ws.rs.core.Response;
import org.example.service.GetMetricsService;
import org.example.service.UpdateMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {
    @Autowired
    UpdateMetricsService updateService;
    @Autowired
    GetMetricsService getService;

    @PostMapping("/spotify/api/metrics/increment/streams")
    public Response incrementStreams(ServletRequest request){
        updateService.incrementStreams(request.getParameter("id"));
        return Response.ok().build();
    }
    @PostMapping("/spotify/api/metrics/increment/likes")
    public Response incrementLikes(ServletRequest request){
        updateService.modifyLikes(request.getParameter("id"), 1);
        return Response.ok().build();
    }

    @PostMapping("/spotify/api/metrics/decrement/likes")
    public Response decrementLikes(ServletRequest request){
        updateService.modifyLikes(request.getParameter("id"), -1);
        return Response.ok().build();
    }
    @GetMapping("/spotify/api/metrics/get/streams/{id}")
    public Response getStreams(@PathVariable String id) throws JsonProcessingException {
        return getService.getSongStreams(id);
    }
    @GetMapping("/spotify/api/metrics/get/likes/{id}")
    public Response getLikes(@PathVariable String id) throws JsonProcessingException {
        return getService.getSongLikes(id);
    }
}
