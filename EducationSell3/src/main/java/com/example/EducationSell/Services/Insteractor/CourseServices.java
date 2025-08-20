package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.DTO.CourseDTO;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.CourseRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Collections;
import java.util.List;

@Service
public class CourseServices {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private UserService userService;

    @Transactional
    public Course addCourse(CourseDTO courseDTO, MultipartFile file, String email) throws IOException {
        User user = userService.findByEmail(email);
        if (user == null) return null;

        Map fileInfo = imageUploadService.uploadImage(file);

        Course course = new Course();
        course.setThumbnailUrl((String) fileInfo.get("secure_url"));
        course.setCourseName(courseDTO.getCourseName());
        course.setDescription(courseDTO.getDescription());
        course.setPrice(courseDTO.getPrice());
        course.setInstructor(user);
        course.setCreatedAt(LocalDateTime.now());

        user.getCreatedCourses().add(course);
        return courseRepository.save(course);
    }

    public Course findById(int id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course with ID " + id + " not found"));
    }

    @Transactional
    public Course updateCourse(Integer id, CourseDTO courseDTO, MultipartFile file) throws IOException {
        Course existingCourse = findById(id);

        if (file != null && !file.isEmpty()) {
            String thumbnailUrl = existingCourse.getThumbnailUrl();
            if (thumbnailUrl != null) {
                String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
                if (thumbnailPublicId != null) {
                    imageUploadService.deleteResource(thumbnailPublicId, "image");
                }
            }
            Map fileInfo = imageUploadService.uploadImage(file);
            existingCourse.setThumbnailUrl((String) fileInfo.get("secure_url"));
        }

        existingCourse.setCourseName(courseDTO.getCourseName());
        existingCourse.setDescription(courseDTO.getDescription());
        existingCourse.setPrice(courseDTO.getPrice());

        return courseRepository.save(existingCourse);
    }

    @Transactional
    public boolean deleteCourse(Integer id) throws IOException {
        Course existingCourse = findById(id);

        String thumbnailUrl = existingCourse.getThumbnailUrl();
        if (thumbnailUrl != null) {
            String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
            if (thumbnailPublicId != null) {
                imageUploadService.deleteResource(thumbnailPublicId, "image");
            }
        }

        User user = existingCourse.getInstructor();
        if (user != null) {
            user.getCreatedCourses().remove(existingCourse);
            userService.save(user);
        }

        courseRepository.delete(existingCourse);
        return true;
    }

    public void save(Course course) {
        courseRepository.save(course);
    }

    public List<Map<String, String>> findByInstructorId(String email) {
        User user = userService.findByEmail(email);
        List<Map<String, String>> l = new ArrayList<>();

        List<Course> courses = user.getCreatedCourses();
        Iterator<Course> iterator = courses.iterator();
        Map<String, String> courseList = null;
        while (iterator.hasNext()) {
            courseList = new HashMap<>();
            Course c = iterator.next();
            courseList.put("courseId",c.getCourseId().toString());
            courseList.put("courseName", c.getCourseName());
            l.add(courseList);
        }


        if (courses.isEmpty()) return null;
        return l;
    }

    public List<CourseDTO> getAllCourses(String email){
        User user = userService.findByEmail(email);
        if(user == null) return null;
//        return user.getCreatedCourses();

        List<CourseDTO> courses = user.getCreatedCourses()
                .stream()
                .map(CourseDTO::new)
                .toList();

        return courses;
    }

    public List<PlaylistDTO> getCoursePlayList(int courseId){


        Course course = findById(courseId);
        if (course == null) return null;

        List<PlaylistDTO> playlistDTOS = course.getPlaylists()
                .stream()
                .map(PlaylistDTO::new)
                .toList();

        return playlistDTOS;
    }

}