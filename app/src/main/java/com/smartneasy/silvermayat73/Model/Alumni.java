package com.smartneasy.silvermayat73.Model;

import com.google.firebase.database.Exclude;

public class Alumni {
    private String name;
    private String imageURL;
    private String key;
    private String description;
    private int position;

    public Alumni() {
        //empty constructor needed
    }
    public Alumni(int position){
        this.position = position;
    }
    public Alumni(String name, String imageUrl , String Des) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageUrl;
        this.description = Des;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getImageUrl() {
        return imageURL;
    }
    public void setImageUrl(String imageUrl) {
        this.imageURL = imageUrl;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }
}

