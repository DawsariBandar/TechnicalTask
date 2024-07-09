package com.task.controller;

import com.task.model.Post;
import com.task.repository.PostRepository;
import com.task.service.ExternalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ExternalApiService externalApiService;
    private final PostRepository postRepository;

    @Autowired
    public ApiController(ExternalApiService externalApiService, PostRepository postRepository) {
        this.externalApiService = externalApiService;
        this.postRepository = postRepository;
    }

    @GetMapping("/fetch-data")
    public ResponseEntity<String> fetchData() {
        externalApiService.fetchPosts().subscribe();
        return ResponseEntity.ok("Data fetched and stored successfully");
    }

    @GetMapping("/data")
    public ResponseEntity<List<Post>> getAllData() {
        List<Post> posts = postRepository.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/data/{id}")
    public ResponseEntity<Post> getDataById(@PathVariable Long id) {
        return postRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
