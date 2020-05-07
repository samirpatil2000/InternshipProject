package com.example.intern.Model;

public class User {
    String id,name,email,coverImage,desc;

    public User() {
    }

    public User(String id, String name, String email, String coverImage, String desc) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.coverImage = coverImage;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
