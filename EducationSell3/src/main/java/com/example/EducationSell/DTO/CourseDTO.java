package com.example.EducationSell.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    @NotBlank(message = "Course name is required")
    @Size(max = 255, message = "Course name must not exceed 255 characters")
    private String courseName;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @DecimalMin(value = "0.00", message = "Price must be at least 0.00")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimal places")
    private BigDecimal price;
}