package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.DTO.CourseDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.CourseRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@Service
public class CourseServices {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private UserService userService;

    public Course addCourse(CourseDTO courseDTO, MultipartFile file,String username) throws IOException {
        User user= userService.findByUserName(username);
        System.out.println(user);
        if(user == null) return null;

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

    public Course findById(int id){
        return courseRepository.findById(id).orElse(null);
    }

}
