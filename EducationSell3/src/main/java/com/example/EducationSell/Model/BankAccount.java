package com.example.EducationSell.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "BankAccounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "bankAccountId")
public class BankAccount {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "BankAccountID")
        private Integer bankAccountId;

    @ManyToOne
    @JoinColumn(name = "InstructorID", nullable = false)
    private User instructor;

        @Column(name = "AccountHolderName", nullable = false, length = 50)
        private String accountHolderName;

        @Column(name = "BankAccountNumber", nullable = false, length = 18)
        private String bankAccountNumber;

        @Column(name = "IFSCCode", nullable = false, length = 11)
        private String ifscCode;

        @Column(name = "CreatedAt", nullable = false)
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
        }
    }

