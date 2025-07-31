package com.example.EducationSell.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Videos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer videoId;

//    @ManyToOne
//    @JoinColumn(name = "CourseID", nullable = false)
//    private Course course;

    @ManyToOne
    @JoinColumn(name = "PlaylistID", nullable = false)
    private Playlist playlist;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "VideoURL", nullable = false, length = 512)
    private String videoUrl;

    @Column(name = "Duration", nullable = false)
    private Integer duration;

    @Column(name = "SequenceOrder", nullable = false)
    private Integer sequenceOrder;

    @Column(name = "ThumbnailURL", length = 512)
    private String thumbnailUrl;

    @Column(name = "VideoStatus", nullable = false)
    private boolean videoStatus;

    @Column(name = "VideoReportNo")
    private Integer videoReportNo;

    @Column(name = "UploadDate", nullable = false, updatable = false)
    private LocalDateTime uploadDate;

    @PrePersist
    protected void onCreate() {
        uploadDate = LocalDateTime.now();
    }
}