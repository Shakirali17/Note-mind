package com.notekeeper.Notemind.repository;

import com.notekeeper.Notemind.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {
    List<Note> findByUserId(String userId); // fetch all notes for a user
}
