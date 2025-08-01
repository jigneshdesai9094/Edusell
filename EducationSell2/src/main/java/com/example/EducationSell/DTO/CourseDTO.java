package com.example.EducationSell.DTO;

import com.example.EducationSell.Model.User;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CourseDTO {

    @NotBlank(message = "Coursename is required")
    private String courseName;

    @NotBlank(message = "Description is required")
    @Size(max = 5000, message = "Description must not exceed 5000 characters")  // Arbitrary limit; adjust as needed
    private String description;


    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.00", message = "Price must be at least 0.00")
    @Digits(integer = 8, fraction = 2, message = "Price must have up to 8 integer digits and 2 decimal places")  // Matches precision=10, scale=2 (8+2=10)
    private BigDecimal price;



}
