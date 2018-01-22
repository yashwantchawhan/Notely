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
    private long time_created;
    private String gist;
    private boolean star;
    private boolean favourite;


    public Note() {

    }

    public Note(NoteBuilder noteBuilder) {
        this.title = noteBuilder.title;
        this.gist = noteBuilder.gist;
        this.type = noteBuilder.type;
        this.time_created = noteBuilder.timeCreated;
        this.star = noteBuilder.isStar;
        this.favourite = noteBuilder.isFavourite;


    }

    public Note(String type, String title, String gist, boolean star, boolean favourite) {
        this.type = type;
        this.title = title;
        this.gist = gist;
        this.star = star;
        this.favourite = favourite;

    }

    public long getTime_created() {
        return time_created;
    }

    public void setTime_created(long time_created) {
        this.time_created = time_created;
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

    public static class NoteBuilder {
        private String title;
        private String gist;
        private String type;
        private long timeCreated;
        private boolean isStar;
        private boolean isFavourite;

        public NoteBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public NoteBuilder setGist(String gist) {
            this.gist = gist;
            return this;
        }

        public NoteBuilder setType(String type) {
            this.type = type;
            return this;
        }

        public NoteBuilder setTimeCreated(long timeCreated) {
            this.timeCreated = timeCreated;
            return this;
        }

        public NoteBuilder isStar(boolean isStar) {
            this.isStar = isStar;
            return this;
        }

        public NoteBuilder isFavourite(boolean isFavourite) {
            this.isFavourite = isFavourite;
            return this;
        }
        public Note build() {
            return new Note(this);
        }
    }
}
