package com.example.review2.models;

import java.io.Serializable;

public class Comic implements Serializable {
    private String id;
    private String nameComic;
    private String art;
    private String desc;
    private String author;
    private String stories;
    private String createdAt;

    public Comic() {
    }
    public Comic(String id, String nameComic, String art, String desc, String author, String stories, String createdAt) {
        this.id = id;
        this.nameComic = nameComic;
        this.art = art;
        this.desc = desc;
        this.author = author;
        this.stories = stories;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameComic() {
        return nameComic;
    }

    public void setNameComic(String nameComic) {
        this.nameComic = nameComic;
    }

    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStories() {
        return stories;
    }

    public void setStories(String stories) {
        this.stories = stories;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comic{" +
                "id='" + id + '\'' +
                ", nameComic='" + nameComic + '\'' +
                ", art='" + art + '\'' +
                ", desc='" + desc + '\'' +
                ", author='" + author + '\'' +
                ", stories='" + stories + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
