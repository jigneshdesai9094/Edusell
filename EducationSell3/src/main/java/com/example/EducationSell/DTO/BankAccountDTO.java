package com.example.EducationSell.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {


    @NotBlank(message = "Account holder name is required")
    @Size(max = 50, message = "Account holder name must not exceed 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Account holder name must contain only letters and spaces")
    private String accountHolderName;

    @NotBlank(message = "Bank account number is required")
    @Size(max = 18, message = "Bank account number must not exceed 18 characters")
    @Pattern(regexp = "^[0-9]+$", message = "Bank account number must contain only digits")
    private String bankAccountNumber;

    @NotBlank(message = "IFSC code is required")
    @Size(min = 11, max = 11, message = "IFSC code must be exactly 11 characters")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    private String ifscCode;
}