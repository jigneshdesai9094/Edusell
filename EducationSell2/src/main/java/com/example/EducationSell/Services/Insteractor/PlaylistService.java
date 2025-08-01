package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.Config.CloudinaryConfig;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.Model.Course;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Model.User;
import com.example.EducationSell.Repository.PlaylistRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataInput;
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

    public Playlist addPlayList(PlaylistDTO playlistDTO, MultipartFile file,String username) throws IOException {

        User user= userService.findByUserName(username);

        if(user == null) {
            return null;
        }

        Course course = courseServices.findById(playlistDTO.getCourseId());
        if(course == null) {
            return null;
        }

        System.out.println("image name is : "+file.getOriginalFilename());
        Map fileInfo = imageUploadService.uploadImage(file);

        Playlist playlist = new Playlist();
        playlist.setPlaylistName(playlistDTO.getPlaylistName());
        playlist.setDescription(playlistDTO.getDescription());
        playlist.setCourse(course);
        playlist.setThumbnailUrl((String) fileInfo.get("secure_url"));

        playlist.setCreatedAt(LocalDateTime.now());

        return playlistRepository.save(playlist);

    }

    public Playlist findById(int id)
    {
       return playlistRepository.findById(id).orElse(null);
    }

}
