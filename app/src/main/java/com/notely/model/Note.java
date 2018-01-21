package com.notely.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by yashwant on 21/01/18.
 */

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String title;
    private String gist;
    private boolean star;
    private boolean favourite;


    public Note(){

    }

    public Note(String type, String title, String gist, boolean star, boolean favourite) {
        this.type = type;
        this.title = title;
        this.gist = gist;
        this.star = star;
        this.favourite = favourite;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGist() {
        return gist;
    }

    public void setGist(String gist) {
        this.gist = gist;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
