package com.example.app.repo;

import com.example.app.models.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableJpaRepositories
public interface ProfileRepository extends CrudRepository<Profile, Long> {
}
