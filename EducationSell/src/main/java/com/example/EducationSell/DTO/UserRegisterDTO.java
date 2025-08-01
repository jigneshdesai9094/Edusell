
package com.example.EducationSell.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Mobile number must be a valid 10-digit number starting with 6, 7, 8, or 9")
    private String mobileNo;

    @NotNull(message = "Role ID is required")
    private Integer roleId;

    public UserRegisterDTO(String username, String email, String mobileNo, LocalDateTime createdAt, Set<String> collect) {
    }
}