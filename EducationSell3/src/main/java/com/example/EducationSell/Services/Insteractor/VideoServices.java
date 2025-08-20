package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.DTO.VideoDTO;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Model.Video;
import com.example.EducationSell.Repository.VideoRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class VideoServices {

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CourseServices courseServices;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private ImageUploadService imageUploadService;

    @Transactional
    public Video uploadVideo(VideoDTO videoDTO, MultipartFile file, MultipartFile videoFile) throws IOException {
        Playlist playlist = playlistService.findById(videoDTO.getPlaylistId());
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist with ID " + videoDTO.getPlaylistId() + " not found");
        }

        Map imageFile = imageUploadService.uploadImage(file);
        Map videoFileResult = imageUploadService.uploadImage(videoFile);

        System.out.println(videoDTO);
        Video video = new Video();
        video.setPlaylist(playlist);
        video.setTitle(videoDTO.getTitle());
        video.setDescription(videoDTO.getDescription());
        video.setDuration((int) Math.round(((Number) videoFileResult.get("duration")).doubleValue()));
        video.setSequenceOrder(videoDTO.getSequenceOrder());
        video.setVideoStatus(true);
        video.setVideoReportNo(0);
        video.setUploadDate(LocalDateTime.now());
        video.setThumbnailUrl((String) imageFile.get("secure_url"));
        video.setVideoUrl((String) videoFileResult.get("secure_url"));
        playlist.getVideos().add(video);

        return videoRepository.save(video);
    }

    @Transactional
    public Video updateVideo(Integer id, VideoDTO videoDTO, MultipartFile thumbnailFile, MultipartFile videoFile) throws IOException {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video with ID " + id + " not found"));

        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbnailUrl = existingVideo.getThumbnailUrl();
            if (thumbnailUrl != null) {
                String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
                if (thumbnailPublicId != null) {
                    imageUploadService.deleteResource(thumbnailPublicId, "image");
                }
            }
            Map imageFile = imageUploadService.uploadImage(thumbnailFile);
            existingVideo.setThumbnailUrl((String) imageFile.get("secure_url"));
        }

        if (videoFile != null && !videoFile.isEmpty()) {
            String videoUrl = existingVideo.getVideoUrl();
            if (videoUrl != null) {
                String videoPublicId = imageUploadService.extractPublicIdFromUrl(videoUrl);
                if (videoPublicId != null) {
                    imageUploadService.deleteResource(videoPublicId, "video");
                }
            }
            Map videoFileResult = imageUploadService.uploadImage(videoFile);
            existingVideo.setVideoUrl((String) videoFileResult.get("secure_url"));
            existingVideo.setDuration((int) Math.round(((Number) videoFileResult.get("duration")).doubleValue()));
        }

        existingVideo.setTitle(videoDTO.getTitle());
        existingVideo.setDescription(videoDTO.getDescription());
        existingVideo.setSequenceOrder(videoDTO.getSequenceOrder());
        Playlist playlist = playlistService.findById(videoDTO.getPlaylistId());
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist with ID " + videoDTO.getPlaylistId() + " not found");
        }
        existingVideo.setPlaylist(playlist);

        return videoRepository.save(existingVideo);
    }

    @Transactional
    public boolean deleteVideo(Integer id) throws IOException {
        Video existingVideo = videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video with ID " + id + " not found"));

        String thumbnailUrl = existingVideo.getThumbnailUrl();
        String videoUrl = existingVideo.getVideoUrl();

        if (thumbnailUrl != null) {
            String thumbnailPublicId = imageUploadService.extractPublicIdFromUrl(thumbnailUrl);
            if (thumbnailPublicId != null) {
                imageUploadService.deleteResource(thumbnailPublicId, "image");
            }
        }

        if (videoUrl != null) {
            String videoPublicId = imageUploadService.extractPublicIdFromUrl(videoUrl);
            if (videoPublicId != null) {
                imageUploadService.deleteResource(videoPublicId, "video");
            }
        }

        Playlist playlist = existingVideo.getPlaylist();
        if (playlist != null) {
            playlist.getVideos().remove(existingVideo);
            playlistService.save(playlist);
        }
        videoRepository.delete(existingVideo);
        return true;
    }
}