package com.example.app.repo;

import com.example.app.models.Post;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.beans.JavaBean;

@Repository
public interface PostRepository extends CrudRepository<Post,Long>{

}
