package com.notekeeper.Notemind.controller;

import com.notekeeper.Notemind.model.Note;
import com.notekeeper.Notemind.service.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note note) {
        Note created = service.createNote(note);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getById(@PathVariable String id) {
        return service.getNoteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Note>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getNotesByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable String id, @RequestBody Note note) {
        try {
            Note updatedNote = service.updateNote(id, note);
            return ResponseEntity.ok(updatedNote);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
