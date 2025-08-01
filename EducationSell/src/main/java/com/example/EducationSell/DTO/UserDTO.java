
package com.example.EducationSell.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
public class UserDTO {
    private String username;
    private String email;
    private String mobileNo;
    private LocalDateTime createdAt;
    private Set<String> roles;
}