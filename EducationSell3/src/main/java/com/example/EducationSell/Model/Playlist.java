package com.example.EducationSell.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "playlistId")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer playlistId;

    @Column(name = "PlaylistName", nullable = false, length = 255)
    private String playlistName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;



    @ManyToOne
    @JoinColumn(name = "CourseID", nullable = false)
    private Course course;

    @Column(name = "ThumbnailURL", length = 255)
    private String thumbnailUrl;

    @OneToOne(mappedBy = "playlist", fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private Note note;


    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @OneToMany(mappedBy = "playlist",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Video> videos = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
