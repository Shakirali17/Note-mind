package com.notekeeper.Notemind.controller;

import com.notekeeper.Notemind.model.Note;
import com.notekeeper.Notemind.service.AssemblyAIService;
import com.notekeeper.Notemind.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/voice")
public class VoiceNoteController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private AssemblyAIService assemblyAIService;

    @GetMapping("/test")
    public String test() {
        return "Voice controller is working!";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadAudio(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam(value = "title", required = false) String title
    ) {
        try {
            // Step 1: Upload to AssemblyAI
            String audioUrl = assemblyAIService.uploadAudio(file.getBytes());

            // Step 2: Request transcription
            String transcriptId = assemblyAIService.requestTranscription(audioUrl);

            // Step 3: Wait for completion
            String transcriptText = assemblyAIService.getTranscriptionResult(transcriptId);

            // Step 4: Save in MongoDB
            Note note = new Note(userId, title != null ? title : "Untitled", transcriptText);

            noteService.createNote(note);

            return ResponseEntity.ok("✅ Note saved successfully! Transcription: " + transcriptText);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("❌ Error: " + e.getMessage());
        }
    }
}
