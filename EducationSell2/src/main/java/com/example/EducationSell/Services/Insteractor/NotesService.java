package com.example.EducationSell.Services.Insteractor;

import com.cloudinary.Cloudinary;
import com.example.EducationSell.Model.Note;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Repository.NoteRepository;
import com.example.EducationSell.Repository.PlaylistRepository;
import com.example.EducationSell.Services.CloudinaryService.NotesUploadService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Data
@AllArgsConstructor
@Service
public class NotesService {
    @Autowired
    private  Cloudinary cloudinary;

    @Autowired
    private  NotesUploadService notesUploadService;

    @Autowired
    private  PlaylistRepository playlistRepository;

    @Autowired
    private NoteRepository noteRepository;

   public String noteUpload(MultipartFile multipartFile,Integer playlistId)
   {
           String noteUrl = notesUploadService.uploadFile(multipartFile);
//           Optional<Playlist> OptionalPlaylist =  playlistRepository.findById(playlistId);
//           Playlist playlist = OptionalPlaylist.get();

           Note note = new Note();
           note.setNoteUrl(noteUrl);
//           note.setPlaylist(playlist);
//           playlist.setNote(note);

           noteRepository.save(note);
//           playlistRepository.save(playlist);
           return noteUrl;
   }

    public Note getNote() {
       return  noteRepository.findById(1).get();
    }
}
