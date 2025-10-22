package com.notekeeper.Notemind.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "notes")
public class Note {

    @Id
    private String id;

    private String userId;
    private String title;

    private String content;   // stores summary separately

    private Instant createdAt;
    private Instant updatedAt;

    public Note() {}

    public Note(String userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

// getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }


    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
