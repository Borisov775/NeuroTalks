package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.models.UserEntity;
import com.example.app.repo.PostRepository;
import com.example.app.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/homepage")
public class PostApiController {

    @Autowired
    private PostRepository repository;

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public Iterable<Post> list() {
        return repository.findAll();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> get(@PathVariable Long id) {
        Optional<Post> p = repository.findById(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/post")
    public ResponseEntity<Post> create(@RequestBody Post post) {
        // Attach author info when available
        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username;
            if (principal instanceof UserDetails) username = ((UserDetails) principal).getUsername();
            else username = principal.toString();
            UserEntity user = userRepo.findByEmail(username);
            if (user != null) {
                post.setNameOfUser(user.getFirstName() + " " + user.getLastName());
            }
        } catch (Exception ignored) {}
        Post saved = repository.save(post);
        return ResponseEntity.created(URI.create("/api/homepage/post/" + saved.getId())).body(saved);
    }
}
