//package com.example.EducationSell.Model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@Entity
//@Table(name="payments")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class Payment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer myorderId;
//
//    private String orderId;
//
//    private long amount;
//
//    private String receipt;
//
//    private String status;
//
//    private String customer_name;
//
//    private String customer_email;
//
//    private String customer_contact;
//
//    private String paymentId;
//
//    @OneToOne
//    @JoinColumn(name = "enrollment_id", nullable = false)
//    private Enrollment enrollment;
//
//}