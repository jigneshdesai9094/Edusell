package com.example.EducationSell.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentUserDetailDTO {

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private Double amount;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @NotBlank(message = "Contact cannot be empty")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Contact must be a valid 10-digit Indian mobile number starting with 6, 7, 8, or 9")
    private String contact;
}