package com.example.EducationSell.Services.Insteractor;

import com.example.EducationSell.Config.CloudinaryConfig;
import com.example.EducationSell.DTO.PlaylistDTO;
import com.example.EducationSell.DTO.VideoDTO;
import com.example.EducationSell.Model.*;
import com.example.EducationSell.Repository.PlaylistRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.UserService;
import jakarta.transaction.Transactional;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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
    public Playlist addPlayList(PlaylistDTO playlistDTO, MultipartFile file, String email) throws IOException {
        User user = userService.findByEmail(email);
        if (user == null) return null;

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

    public List<Map<String,String>> getPlayListName(String email){
        User user = userService.findByEmail(email);
        List<Map<String,String>> playlist = new ArrayList<>();

        List<Course> courses = user.getCreatedCourses();
        Iterator<Course> iterator = courses.iterator();
        Map<String, String> playListName = null;
        List<Playlist> playlists = null;
        while (iterator.hasNext()) {
            Course c = iterator.next();
            playlists = c.getPlaylists();
            Iterator<Playlist> iteratorPlaylist = playlists.iterator();

            while (iteratorPlaylist.hasNext()){
                playListName = new HashMap<>();
                Playlist p = iteratorPlaylist.next();
                playListName.put("playlistId",p.getPlaylistId().toString());
                playListName.put("playlistName",p.getPlaylistName());
                playlist.add(playListName);
            }
        }


        if(playlist.isEmpty() ) return null;
        System.out.println(playlist);
        return playlist;
    }

    public Note getNoteByPlaylist(Integer id) throws IllegalArgumentException {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        Playlist playlist = optionalPlaylist.get();
        if(playlist==null) {
            throw new IllegalArgumentException("Playlist with ID " + id + " not found");
        }
        return  playlist.getNote();
    }

    public List<VideoDTO> getVideosByPlaylist(Integer id) throws IllegalArgumentException {
        Optional<Playlist> optionalPlaylist = playlistRepository.findById(id);
        Playlist playlist = optionalPlaylist.get();
        if(playlist==null) {
            throw new IllegalArgumentException("Playlist with ID " + id + " not found");
        }
        List<Video> videos = playlist.getVideos();



        Iterator<Video> iterator = videos.iterator();
        List<VideoDTO> videoDTOS = new LinkedList<>();
        while(iterator.hasNext())
        {
            VideoDTO videoDTO = new VideoDTO();
            Video v = iterator.next();
            videoDTO.setVideoId(v.getVideoId());
            videoDTO.setPlaylistId(v.getPlaylist().getPlaylistId());
            videoDTO.setTitle(v.getTitle());

            videoDTO.setVideoReportNo(v.getVideoReportNo());
            videoDTO.setVideoStatus(v.isVideoStatus());
            videoDTO.setDescription(v.getDescription());

            videoDTO.setSequenceOrder(v.getSequenceOrder());
            videoDTO.setUploadDate(v.getUploadDate());
            videoDTO.setVideoUrl(v.getVideoUrl());
            videoDTO.setThumbnailUrl(v.getThumbnailUrl());


            videoDTO.setDuration(v.getDuration());
            videoDTOS.add(videoDTO);

        }
        return  videoDTOS;
    }

}