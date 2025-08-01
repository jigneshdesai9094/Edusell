package com.example.EducationSell.Controller;

import com.example.EducationSell.Model.Note;
import com.example.EducationSell.Services.CloudinaryService.NotesUploadService;
import com.example.EducationSell.Services.Insteractor.NotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/instructor")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @RequestMapping("/upload-note")
    public ResponseEntity<?> uploadNotes(@RequestPart("note") MultipartFile file,Integer playlistId) {
        try {

            String notesUrl = notesService.noteUpload(file,playlistId);

            return  new ResponseEntity<>("Note uploaded successfully",HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping("/getNote")
    public ResponseEntity<?> getNote(){

        try {
            Note note = notesService.getNote();
            Resource resource = new UrlResource(note.getNoteUrl());
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"course_notes_" + System.currentTimeMillis() + ".pdf\"");
            headers.add("Content-Type", "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(resource.contentLength())
                    .body(resource);
        }catch (Exception e)
        {
            return  new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}