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
    private String transcription;
    private String summary;
    private String transcriptId;// stores summary separately
    private Instant createdAt;
    private Instant updatedAt;

    public Note() {}

    public Note(String userId, String title, String transcription, String summary, String transcriptId) {
        this.userId = userId;
        this.title = title;
        this.transcription = transcription;
        this.summary = summary;
        this.transcriptId = transcriptId;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

// getters and setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTranscription() {return transcription;}
    public void setTranscription(String transcription) {this.transcription = transcription;}

    public String getSummary() {return summary;}
    public void setSummary(String summary) {this.summary = summary;}

    public String getTranscriptId() {return transcriptId;}
    public void setTranscriptId(String transcriptId) {this.transcriptId = transcriptId;}

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
