package com.example.EducationSell.Controller;

import com.example.EducationSell.DTO.CourseDTO;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.Insteractor.CourseServices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

@RequestMapping("/course")
@CrossOrigin(origins = "http://localhost:5173",allowCredentials = "true")

public class CourseController {

    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;

    @Autowired
    private CourseServices courseServices;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/addCourse", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addCourse(@RequestPart("image") MultipartFile file,
                                       @RequestPart("courseDetails") String courseDTO) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();


        // File validation
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

        // Validate JSON deserialization
        CourseDTO courseDetailsDto;
        try {
            courseDetailsDto = objectMapper.readValue(courseDTO, CourseDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid course details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        // Validate DTO
        // Note: Validation should be done in service layer or by a validator, but we handle it after deserialization
        Course courseAdded = courseServices.addCourse(courseDetailsDto, file, email);

        if(courseAdded == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return ResponseEntity.ok(courseAdded);
    }

    @PutMapping(value = "/updateCourse/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCourse(@PathVariable("id") Integer id,
                                          @RequestPart(value = "image", required = false) MultipartFile file,
                                          @RequestPart("courseDetails") String courseDTO) throws IOException {
        // File validation if provided
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

        // Validate JSON deserialization
        CourseDTO courseDetailsDto;
        try {
            courseDetailsDto = objectMapper.readValue(courseDTO, CourseDTO.class);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Invalid course details JSON format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        Course updatedCourse = courseServices.updateCourse(id, courseDetailsDto, file);
        return ResponseEntity.ok(updatedCourse);
    }

    @DeleteMapping("/deleteCourse/{id}")
    public ResponseEntity<?> deleteCourse(@PathVariable("id") Integer id) throws IOException {
        boolean deleted = courseServices.deleteCourse(id);
        if (deleted) {
            return ResponseEntity.ok("Course and associated playlists deleted successfully");
        }
        return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/listOfCourse")
    public ResponseEntity<?> fetchMyCourse(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<Map<String, String>> listOfCourse = courseServices.findByInstructorId(email);
        if(listOfCourse == null){
            return new ResponseEntity<>(listOfCourse,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(listOfCourse,HttpStatus.OK);

    }

    @GetMapping("/getAllCourses")
    public ResponseEntity<?> getAllCourses(){
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        List<CourseDTO> listOfCourses = courseServices.getAllCourses(email);

//        if(listOfCourses.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(listOfCourses,HttpStatus.OK);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<?> getPlaylist(@PathVariable String course_id){
        try {
            int courseId = Integer.parseInt(course_id);
            List<PlaylistDTO> playlists = courseServices.getCoursePlayList(courseId);
            return new ResponseEntity<>(playlists,HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Something wen't wrong",HttpStatus.BAD_REQUEST);
        }
    }
}