package com.notekeeper.Notemind.controller;

import com.notekeeper.Notemind.model.Note;
import com.notekeeper.Notemind.service.SummarizerService;
import com.notekeeper.Notemind.service.TranscriptionService;
import com.notekeeper.Notemind.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/voice")
public class TranscriptionController {

    @Autowired
    private NoteService noteService;

    @Autowired
    private TranscriptionService assemblyAIService;

    @Autowired
    private SummarizerService summarizeService;

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

            // Step 2: Request transcription (with summarization enabled in service)
            String transcriptId = assemblyAIService.requestTranscription(audioUrl);

            // Step 3: Wait for transcription + summary completion
            Map<String, String> transcriptData = assemblyAIService.getTranscriptionResult(transcriptId);
            String transcriptText = transcriptData.get("text");
            String summaryText = transcriptData.get("summary");

            // Step 4: Save in MongoDB
            Note note = new Note(
                    userId,
                    title != null ? title : "Untitled",
                    transcriptText,
                    summaryText
            );

            noteService.createNote(note);

            // Step 5: Send back response
            return ResponseEntity.ok(Map.of(
                    "message", "✅ Note saved successfully!",
                    "transcription", transcriptText,
                    "summary", summaryText
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }
}
