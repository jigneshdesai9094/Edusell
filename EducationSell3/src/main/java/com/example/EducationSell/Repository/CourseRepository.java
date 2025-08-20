package com.example.EducationSell.Repository;


import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

//    List<Course> findByInstructorId(User id);
}
