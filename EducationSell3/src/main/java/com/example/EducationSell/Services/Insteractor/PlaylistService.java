package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.Config.CloudinaryConfig;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.PlaylistRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class PlaylistService {

    @Autowired
    private CloudinaryConfig cloudinaryConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private CourseServices courseServices;

    @Transactional
    public Playlist addPlayList(PlaylistDTO playlistDTO, MultipartFile file, int id) throws IOException {
        User user = userService.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + id + " not found");
        }

        Course course = courseServices.findById(playlistDTO.getCourseId());
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + playlistDTO.getCourseId() + " not found");
        }

        Map fileInfo = imageUploadService.uploadImage(file);

        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistDTO.getPlaylistName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setCourse(course);
        playlist.setThumbnailUrl((String) fileInfo.get("secure_url"));
        playlist.setCreatedAt(LocalDateTime.now());
        course.getPlaylists().add(playlist);

        playlistRepository.save(playlist);
        courseServices.save(course);
        return playlist;
    }

    public Playlist findById(int id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Playlist with ID " + id + " not found"));
    }

    @Transactional
    public Playlist updatePlaylist(Integer id, PlaylistDTO playlistDTO, MultipartFile file) throws IOException {
        Playlist existingPlaylist = findById(id);

        if (file != null && !file.isEmpty()) {
            String thumbnailUrl = existingPlaylist.getThumbnailUrl();
            if (thumbnailUrl != null) {
                String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
                if (thumbnailPublicId != null) {
                    imageUploadService.deleteResource(thumbnailPublicId, "image");
                }
            }
            Map fileInfo = imageUploadService.uploadImage(file);
            existingPlaylist.setThumbnailUrl((String) fileInfo.get("secure_url"));
        }

        existingPlaylist.setPlaylistName(playlistDTO.getPlaylistName());
        existingPlaylist.setDescription(playlistDTO.getDescription());
        Course course = courseServices.findById(playlistDTO.getCourseId());
        if (course == null) {
            throw new IllegalArgumentException("Course with ID " + playlistDTO.getCourseId() + " not found");
        }
        existingPlaylist.setCourse(course);

        return playlistRepository.save(existingPlaylist);
    }

    @Transactional
    public boolean deletePlaylist(Integer id) throws IOException {
        Playlist existingPlaylist = findById(id);

        String thumbnailUrl = existingPlaylist.getThumbnailUrl();
        if (thumbnailUrl != null) {
            String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
            if (thumbnailPublicId != null) {
                imageUploadService.deleteResource(thumbnailPublicId, "image");
            }
        }

        Course course = existingPlaylist.getCourse();
        if (course != null) {
            course.getPlaylists().remove(existingPlaylist);
            courseServices.save(course);
        }

        playlistRepository.delete(existingPlaylist);
        return true;
    }

    public void save(Playlist playlist) {
        playlistRepository.save(playlist);
    }
}