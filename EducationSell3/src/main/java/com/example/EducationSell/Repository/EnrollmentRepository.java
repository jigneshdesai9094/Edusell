package com.example.EducationSell.Repository;

import com.example.EducationSell.Model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment,Integer> {
}
