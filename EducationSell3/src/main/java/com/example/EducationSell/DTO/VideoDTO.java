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

    @NotNull(message = "Playlist ID is required")
    @Positive(message = "Playlist ID must be a positive number")
    private Integer playlistId;

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @NotNull(message = "Sequence order is required")
    @Positive(message = "Sequence order must be a positive number")
    private Integer sequenceOrder;

    private boolean videoStatus;

    @PositiveOrZero(message = "Video report number must be zero or positive")
    private Integer videoReportNo;

    private LocalDateTime uploadDate;
    private Integer videoId;
    private String videoUrl;
    private String thumbnailUrl;
    private Integer duration;
}