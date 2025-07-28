package com.example.EducationSell.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {

    @NotBlank(message = "Playlist name is required")
    @Size(max = 255, message = "Playlist name must not exceed 255 characters")
    private String playlistName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

//    @NotNull(message = "Instructor ID is required")
//    @Positive(message = "Instructor ID must be a positive number")
//    private Integer instructorId;

    @NotNull(message = "Course ID is required")
    @Positive(message = "Course ID must be a positive number")
    private Integer courseId;

//
//    @Size(max = 255, message = "Thumbnail URL must not exceed 255 characters")
//    private String thumbnailUrl;

    private LocalDateTime createdAt;
}