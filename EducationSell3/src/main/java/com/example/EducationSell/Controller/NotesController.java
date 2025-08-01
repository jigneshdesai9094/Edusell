package com.example.EducationSell.Controller;

import com.example.EducationSell.Model.Note;
import com.example.EducationSell.Services.Insteractor.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/instructor")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/upload-note")
    public ResponseEntity<?> uploadNotes(@RequestPart("note") MultipartFile file, @RequestParam Integer playlistId) throws IOException {
        Note notesUrl = notesService.noteUpload(file, playlistId);
        return ResponseEntity.ok(notesUrl);
    }

    @GetMapping("/getNote")
    public ResponseEntity<?> getNote() throws IOException {
        Note note = notesService.getNote(1);
        Resource resource = new UrlResource(note.getNoteUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"course_notes_" + System.currentTimeMillis() + ".pdf\"");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }

    @DeleteMapping("/deleteNote/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable("id") Integer id) throws IOException {
        boolean deleted = notesService.deleteNote(id);
        if (deleted) {
            return ResponseEntity.ok("Note deleted successfully");
        }
        return new ResponseEntity<>("Note not found", HttpStatus.NOT_FOUND);
    }
}