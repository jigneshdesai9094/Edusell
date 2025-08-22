package com.example.EducationSell.Controller;

import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.DTO.VideoDTO;
import com.example.EducationSell.Model.Note;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Services.Insteractor.NotesService;
import com.example.EducationSell.Services.Insteractor.PlaylistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlist")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")
public class PlaylistController {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private NotesService notesService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/addPlayList", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPlayList(@RequestPart("image") MultipartFile file,
                                         @RequestPart("playlistDetails") String playlistDetails,
                                         @RequestPart(value = "note", required = false) MultipartFile note) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>("File is required and cannot be empty", HttpStatus.BAD_REQUEST);
        }
        if (file.getSize() > MAX_FILE_SIZE) {
            return new ResponseEntity<>("File size exceeds the maximum limit of 2MB", HttpStatus.BAD_REQUEST);
        }
        String contentType = file.getContentType();
        if (contentType == null || !Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
            return new ResponseEntity<>("Invalid file type. Only JPG or PNG images are allowed.", HttpStatus.BAD_REQUEST);
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") || originalFilename.endsWith(".png"))) {
            return new ResponseEntity<>("File must be a JPG or PNG.", HttpStatus.BAD_REQUEST);
        }

        PlaylistDTO playlistDTO;
        try {
            playlistDTO = objectMapper.readValue(playlistDetails, PlaylistDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid playlist details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Playlist isAdded = playlistService.addPlayList(playlistDTO, file, email);

        if(isAdded == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(note!=null)
        {
            System.out.println("note is not null and added");
            notesService.noteUpload(note, isAdded.getPlaylistId());
        }
        else {
            System.out.println("note is null");
        }
        return ResponseEntity.ok(isAdded);
    }

    @PutMapping(value = "/updatePlaylist/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePlaylist(@PathVariable("id") Integer id,
                                            @RequestPart(value = "image", required = false) MultipartFile file,
                                            @RequestPart("playlistDetails") String playlistDetails) throws IOException {
        if (file != null && !file.isEmpty()) {
            if (file.getSize() > MAX_FILE_SIZE) {
                return new ResponseEntity<>("File size exceeds the maximum limit of 2MB", HttpStatus.BAD_REQUEST);
            }
            String contentType = file.getContentType();
            if (contentType == null || !Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
                return new ResponseEntity<>("Invalid file type. Only JPG or PNG images are allowed.", HttpStatus.BAD_REQUEST);
            }
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") || originalFilename.endsWith(".png"))) {
                return new ResponseEntity<>("File must be a JPG or PNG.", HttpStatus.BAD_REQUEST);
            }
        }

        PlaylistDTO playlistDTO;
        try {
            playlistDTO = objectMapper.readValue(playlistDetails, PlaylistDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid playlist details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Playlist updatedPlaylist = playlistService.updatePlaylist(id, playlistDTO, file);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/deletePlaylist/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable("id") Integer id) throws IOException {
        boolean deleted = playlistService.deletePlaylist(id);
        if (deleted) {
            return ResponseEntity.ok("Playlist and associated note deleted successfully");
        }
        return new ResponseEntity<>("Playlist not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getPlaylistName")
    public ResponseEntity<?> getPlayListName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<Map<String,String>> playlistName = playlistService.getPlayListName(email);

        if(playlistName.isEmpty()) return new ResponseEntity<>("Playlist Not Found",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(playlistName,HttpStatus.OK);
    }

    @GetMapping("/getNote/{id}")
    public ResponseEntity<?> getNoteByPlaylist(@PathVariable("id") Integer id) throws IOException{
        Note note = playlistService.getNoteByPlaylist(id);
        if (note == null) {
            return new ResponseEntity<>("No Note found for playlist ID: " + id, HttpStatus.NOT_FOUND);
        }
        Resource resource = new UrlResource(note.getNoteUrl());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"course_notes_" + System.currentTimeMillis() + ".pdf\"");
        headers.add("Content-Type", "application/pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.contentLength())
                .body(resource);
    }


    @GetMapping("/getVideos/{id}")
    public ResponseEntity<?> getVideosByPlaylist(@PathVariable("id") Integer id) {

        List<VideoDTO> videos = playlistService.getVideosByPlaylist(id);
        return ResponseEntity.ok(videos);
    }


}