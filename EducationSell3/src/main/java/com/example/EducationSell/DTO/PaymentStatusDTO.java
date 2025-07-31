package com.example.EducationSell.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusDTO {

    @NotBlank(message = "Order ID cannot be empty")
    private String orderId;

    @NotBlank(message = "Status cannot be empty")
    @Pattern(regexp = "^(created|captured|failed|refunded)$", message = "Status must be one of: created, captured, failed, refunded")
    private String status;

    @NotBlank(message = "Payment ID cannot be empty")
    private String paymentId;

    @NotNull(message = "Course ID cannot be null")
    @Positive(message = "Course ID must be a positive integer")
    private Integer courseId;
}