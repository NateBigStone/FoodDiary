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

    Food(@NonNull String title, String description, String imagePath, String tags) {
        this.title = title;
        this.dateCreated = Long.toString(new Date().getTime());

                //DateFormat.getDateInstance().format(new Date());
        this.description = description;
        this.imagePath = imagePath;
        this.tags = tags;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

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
}
