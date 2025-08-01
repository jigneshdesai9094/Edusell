package com.example.EducationSell.Controller;


import com.example.EducationSell.DTO.CourseDTO;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Services.Insteractor.PlaylistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;


    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private ObjectMapper objectMapper;


    @PostMapping(value = "/addPlayList",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPlayList(@RequestPart("image") MultipartFile file,
                                         @RequestPart("playlistDetails") String playlistDetails) throws IOException {

        Authentication authenticationManager = SecurityContextHolder.getContext().getAuthentication();
        String username = authenticationManager.getName();

        // File validation
        System.out.println(playlistDetails);
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
            return new ResponseEntity<>("Invalid course details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Playlist isAdded = playlistService.addPlayList(playlistDTO,file,username);
        if(isAdded != null)
        {
            return new ResponseEntity<>(isAdded,HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
}
