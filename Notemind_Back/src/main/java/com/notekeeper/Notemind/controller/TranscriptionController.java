package com.notekeeper.Notemind.controller;

import com.notekeeper.Notemind.model.Note;
import com.notekeeper.Notemind.service.SummarizerService;
import com.notekeeper.Notemind.service.TranscriptionService;
import com.notekeeper.Notemind.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.HashMap;
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

            // Step 2: Request transcription
            String transcriptId = assemblyAIService.requestTranscription(audioUrl);

            // Step 3: Fetch transcription result
            Map<String, String> transcriptionData = assemblyAIService.getTranscriptionResult(transcriptId);
            String transcriptText = transcriptionData.get("text");

            // ✅ Create Note object correctly
            Note note = new Note(
                    userId,
                    title != null ? title : "Untitled",
                    transcriptText,// ✅ fixed here
                    null,           // summary will come later…
                    transcriptId
            );

            // ✅ Save Note and get saved object
            Note savedNote = noteService.createNote(note);

            return ResponseEntity.ok(Map.of(
                    "message", "✅ Transcription done! Summary can be generated later.",
                    "noteId", savedNote.getId(), // ✅ fixed here
                    "transcription", transcriptText
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("❌ Error: " + e.getMessage());
        }
    }

    @PostMapping("/summarize/{noteId}")
    public ResponseEntity<Map<String, Object>> summarizeNote(@PathVariable String noteId) {
        Map<String, Object> response = new HashMap<>();

        return noteService.getNoteById(noteId).map(note -> {

            if (note.getTranscriptId() == null) {
                response.put("error", "❌ No transcriptId found. Summarization not possible.");
                return ResponseEntity.badRequest().body(response);
            }

//            if (!summarizeService.isTranscriptReady(note.getTranscriptId())) {
//                Map<String, Object> resp= new HashMap<>();
//                resp.put("error", "⏳ Transcript is still processing. Try again after a few seconds!");
//                return ResponseEntity.status(HttpStatus.ACCEPTED).body(resp);
//            }

// ✅ Transcript is ready — now summarize!
            String summary = summarizeService.summarizeTranscript(note.getTranscriptId());


            note.setSummary(summary);
            note.setUpdatedAt(Instant.now());
            noteService.createNote(note); // ✅ Update note with summary

            response.put("message", "✅ Summary generated and saved!");
            response.put("noteId", noteId);
            response.put("summary", summary);

            return ResponseEntity.ok(response);

        }).orElseGet(() -> {
            response.put("error", "❌ Note not found!");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        });
    }
}
