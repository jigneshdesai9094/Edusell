package com.example.EducationSell.Services.Learner;

import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Enrollment;
import com.example.EducationSell.Model.Payment;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollementService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Transactional
    public Enrollment saveEnrollement(User user, Course course, Payment payment) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setPayment(payment);
        course.getEnrollments().add(enrollment);
        user.getEnrollments().add(enrollment);
        return enrollmentRepository.save(enrollment);
    }
}