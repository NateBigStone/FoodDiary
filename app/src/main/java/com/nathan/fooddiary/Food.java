package com.nathan.fooddiary;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

@Entity
public class Food {

    private String title;

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @PrimaryKey
    @NonNull
    private String dateCreated;
    private String description;
    private String imagePath;
    private String tags;

    Food(String title, String description, String imagePath, String tags) {
        this.title = title;
        this.dateCreated = Long.toString(new Date().getTime());
        this.description = description;
        this.imagePath = imagePath;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    @NonNull
    public String getDateCreated() {
        return dateCreated;
    }

    public String getDescription(){
        return description;
    }

    public String getImagePath(){
        return imagePath;
    }

    public String getTags(){
        return tags;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setImagePath(String imagePath){
        this.imagePath = imagePath;
    }

    public void setTags(String tags){
        this.tags = tags;
    }
}
