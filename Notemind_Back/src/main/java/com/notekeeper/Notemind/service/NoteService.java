package com.notekeeper.Notemind.service;

import com.notekeeper.Notemind.model.Note;
import com.notekeeper.Notemind.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public Note createNote(Note note) {
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());
        return repo.save(note);
    }

    public Optional<Note> getNoteById(String id) {
        return repo.findById(id);
    }

    public List<Note> getNotesByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    public Note updateNote(String id, Note update) {
        return repo.findById(id).map(existing -> {
            if (update.getTitle() != null) existing.setTitle(update.getTitle());
            if (update.getTranscription() != null) existing.setTranscription(update.getTranscription());
            existing.setUpdatedAt(Instant.now());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Note not found"));
    }

    public void deleteNote(String id) {
        repo.deleteById(id);
    }
}
