package com.example.app.models;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;
import java.sql.Blob;

@Entity
public class Profile {

    public Profile(){

    }
    public Profile(String email, String bio, String link1, String link2){
        this.email=email;
        this.bio=bio;
        this.link1=link1;
        this.link2=link2;
    }
    private String email,bio,link1,link2;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long profile_id;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLink1() {
        return link1;
    }

    public void setLink1(String link1) {
        this.link1 = link1;
    }

    public String getLink2() {
        return link2;
    }

    public void setLink2(String link2) {
        this.link2 = link2;
    }

    public Long getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(Long profile_id) {
        this.profile_id = profile_id;
    }
}
