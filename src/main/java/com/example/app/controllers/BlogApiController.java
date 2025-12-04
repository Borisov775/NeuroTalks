package com.example.app.controllers;

import com.example.app.models.Post;
import com.example.app.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/blog")
public class BlogApiController {

    @Autowired
    private PostRepository repository;

    @GetMapping
    public Iterable<Post> list() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable Long id) {
        Optional<Post> p = repository.findById(id);
        return p.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post) {
        Post saved = repository.save(post);
        return ResponseEntity.created(URI.create("/api/blog/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post updated) {
        Optional<Post> existing = repository.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Post p = existing.get();
        p.setTopic(updated.getTopic());
        p.setFull_text(updated.getFull_text());
        p.setUrlImage(updated.getUrlImage());
        repository.save(p);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Post> existing = repository.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        repository.delete(existing.get());
        return ResponseEntity.noContent().build();
    }
}
