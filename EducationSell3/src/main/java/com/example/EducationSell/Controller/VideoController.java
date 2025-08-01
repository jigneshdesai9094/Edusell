package com.example.EducationSell.Controller;

import com.example.EducationSell.DTO.VideoDTO;
import com.example.EducationSell.Model.Video;
import com.example.EducationSell.Services.Insteractor.VideoServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/video")
public class VideoController {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Autowired
    private VideoServices videoServices;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/uploadVideo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addVideo(@RequestPart("image") MultipartFile file,
                                      @RequestPart("video") MultipartFile video,
                                      @RequestPart("videoDetails") String videoDetails) throws IOException {
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

        VideoDTO videoDTO;
        try {
            videoDTO = objectMapper.readValue(videoDetails, VideoDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid video details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Video isVideoUploaded = videoServices.uploadVideo(videoDTO, file, video);
        return ResponseEntity.ok(isVideoUploaded);
    }

    @PutMapping(value = "/updateVideo/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateVideo(@PathVariable("id") Integer id,
                                         @RequestPart(value = "image", required = false) MultipartFile thumbnailFile,
                                         @RequestPart(value = "video", required = false) MultipartFile videoFile,
                                         @RequestPart("videoDetails") String videoDetails) throws IOException {
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            if (thumbnailFile.getSize() > MAX_FILE_SIZE) {
                return new ResponseEntity<>("Thumbnail file size exceeds the maximum limit of 2MB", HttpStatus.BAD_REQUEST);
            }
            String contentType = thumbnailFile.getContentType();
            if (contentType == null || !Arrays.asList("image/jpeg", "image/png").contains(contentType)) {
                return new ResponseEntity<>("Invalid thumbnail file type. Only JPG or PNG images are allowed.", HttpStatus.BAD_REQUEST);
            }
            String originalFilename = thumbnailFile.getOriginalFilename();
            if (originalFilename == null || !(originalFilename.endsWith(".jpg") || originalFilename.endsWith(".jpeg") || originalFilename.endsWith(".png"))) {
                return new ResponseEntity<>("Thumbnail file must be a JPG or PNG.", HttpStatus.BAD_REQUEST);
            }
        }

        VideoDTO videoDTO;
        try {
            videoDTO = objectMapper.readValue(videoDetails, VideoDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid video details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Video updatedVideo = videoServices.updateVideo(id, videoDTO, thumbnailFile, videoFile);
        return ResponseEntity.ok(updatedVideo);
    }

    @DeleteMapping("/deleteVideo/{id}")
    public ResponseEntity<?> deleteVideo(@PathVariable("id") Integer id) throws IOException {
        boolean deleted = videoServices.deleteVideo(id);
        if (deleted) {
            return ResponseEntity.ok("Video deleted successfully");
        }
        return new ResponseEntity<>("Video not found", HttpStatus.NOT_FOUND);
    }
}