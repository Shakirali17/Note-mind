package com.notekeeper.Notemind.model;

public class TranscriptionResult {
    private String transcription;
    private String summary;

    public TranscriptionResult(String transcription, String summary) {
        this.transcription = transcription;
        this.summary = summary;
    }

    public String getTranscription() { return transcription; }
    public String getSummary() { return summary; }

    public void setTranscription(String transcription) { this.transcription = transcription; }
    public void setSummary(String summary) { this.summary = summary; }
}
