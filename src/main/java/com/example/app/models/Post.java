package com.example.app.models;

import org.hibernate.type.descriptor.sql.BlobTypeDescriptor;
import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Blob;
import java.time.LocalDate;

@Entity
public class Post {


    public Post(){

    }
    public Post(String topic, String full_text, String urlImage) {
        this.topic = topic;
        this.full_text = full_text;
        this.urlImage=urlImage;
    }
    public Post(String topic,String full_text, String urlImage,String nameOfUser,String email, String timeOfCreation){
        this.topic = topic;
        this.full_text = full_text;
        this.urlImage=urlImage;
        this.nameOfUser=nameOfUser;
        this.email=email;
        this.timeOfCreation=timeOfCreation;
    }
    public Post(String topic,String full_text, String urlImage,String nameOfUser,String email, String timeOfCreation,String theme,String shortDescription,Long profile_id){
        this.topic = topic;
        this.full_text = full_text;
        this.urlImage=urlImage;
        this.nameOfUser=nameOfUser;
        this.email=email;
        this.timeOfCreation=timeOfCreation;
        this.theme=theme;
        this.shortDescription=shortDescription;
        this.profile_id=profile_id;
    }



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long profile_id;
    private String topic,full_text,urlImage,nameOfUser,email,timeOfCreation,theme,shortDescription;
    private int views;

    public Long getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(Long profile_id) {
        this.profile_id = profile_id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getNameOfUser() {
        return nameOfUser;
    }

    public void setNameOfUser(String nameOfUser) {
        this.nameOfUser = nameOfUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(String timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getFull_text() {
        return full_text;
    }

    public void setFull_text(String full_text) {
        this.full_text = full_text;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
