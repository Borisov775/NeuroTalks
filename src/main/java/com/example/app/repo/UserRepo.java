package com.example.app.repo;


import com.example.app.models.Post;
import com.example.app.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@EnableJpaRepositories
public interface UserRepo extends JpaRepository<UserEntity,Post> {
    UserEntity findByEmail(String email);
    Post findById(Long id);
}