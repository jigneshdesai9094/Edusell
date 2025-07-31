package com.example.EducationSell.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO {

//    @NotNull(message = "Course ID is required")
//    @Positive(message = "Course ID must be a positive number")
//    private Integer courseId;

    @NotNull(message = "Playlist ID is required")
    @Positive(message = "Playlist ID must be a positive number")
    private Integer playlistId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

//    @Size(max = 512, message = "Video URL must not exceed 512 characters")
//    @Pattern(regexp = "^https?://.*\\.(mp4|mov|avi|wmv|mkv|flv|webm)$", message = "Video URL must point to a valid video format (e.g., .mp4, .mov, .avi, .wmv, .mkv, .flv, .webm)")
//    private String videoUrl;

//    @Positive(message = "Duration must be a positive number")
//    private Integer duration;

    @NotNull(message = "Sequence order is required")
    @Positive(message = "Sequence order must be a positive number")
    private Integer sequenceOrder;

//    @Size(max = 512, message = "Thumbnail URL must not exceed 512 characters")
//    private String thumbnailUrl;

    private boolean videoStatus;

    @Positive(message = "Video report number must be a positive number")
    private Integer videoReportNo;

    private LocalDateTime uploadDate;
}