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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
    @PostMapping(value = "/uploadVideo",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

        VideoDTO videoDTO = objectMapper.readValue(videoDetails,VideoDTO.class);

        Video isVideoUploaded = videoServices.uploadVideo(videoDTO,file,video);
        if(isVideoUploaded != null){
            return new ResponseEntity<>(isVideoUploaded,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}