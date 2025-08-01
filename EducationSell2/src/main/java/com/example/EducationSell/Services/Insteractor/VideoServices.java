package com.example.EducationSell.Services.Insteractor;


import com.example.EducationSell.DTO.VideoDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Model.Video;
import com.example.EducationSell.Repository.VideoRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
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

    public Video uploadVideo(VideoDTO videoDTO,MultipartFile file,MultipartFile videoFile) throws IOException {
        Video video = new Video();

//        Course course = courseServices.findById(videoDTO.getCourseId());
//        if(course == null) return null;

        Playlist playlist = playlistService.findById(videoDTO.getPlaylistId());
        if(playlist == null) return null;


        Map imageFile = imageUploadService.uploadImage(file);
        Map videoFileResult = imageUploadService.uploadImage(videoFile);

        System.out.println(videoFileResult);

//        video.setCourse(course);
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

        return videoRepository.save(video);
    }



}