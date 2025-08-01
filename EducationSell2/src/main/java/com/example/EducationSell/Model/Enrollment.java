//package com.example.EducationSell.Model;
//
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//import java.util.UUID;
//
//@Entity
//@Table(name = "enrollments")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Enrollment {
//    @Id
//    @GeneratedValue
//    @Column(columnDefinition = "UUID")
//    private Integer id;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user; // Changed from Learner
//
//    @ManyToOne
//    @JoinColumn(name = "course_id", nullable = false)
//    private Course course;
//
//    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Payment payment;
//
//    @Column(name = "CreatedAt", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//    }
//}