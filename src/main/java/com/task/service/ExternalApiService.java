package com.task.service;

import com.task.model.Post;
import com.task.model.User;
import com.task.repository.PostRepository;
import com.task.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class ExternalApiService {
    private final WebClient webClient;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final Random random = new Random();

    @Autowired
    public ExternalApiService(WebClient.Builder webClientBuilder, PostRepository postRepository, UserRepository userRepository) {
        this.webClient = webClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build();
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Flux<Post> fetchPosts() {
        return this.webClient.get()
                .uri("/posts")
                .retrieve()
                .bodyToFlux(Post.class)
                .flatMap(this::savePost);
    }

    private Mono<Post> savePost(Post post) {
        return this.webClient.get()
                .uri("/users/{id}", post.getUserId())
                .retrieve()
                .bodyToMono(User.class)
                .flatMap(user -> {
                    user.setPassword("password" + random.nextInt(1000));
                    user = userRepository.save(user);
                    post.setUserId(user.getId());
                    return Mono.just(postRepository.save(post));
                });
    }
}
