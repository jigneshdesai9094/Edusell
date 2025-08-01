package com.example.EducationSell.Services.Insteractor;

import com.cloudinary.Cloudinary;
import com.example.EducationSell.Model.Note;
import com.example.EducationSell.Model.Playlist;
import com.example.EducationSell.Repository.NoteRepository;
import com.example.EducationSell.Services.CloudinaryService.ImageUploadService;
import com.example.EducationSell.Services.CloudinaryService.NotesUploadService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class NotesService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private NotesUploadService notesUploadService;

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private ImageUploadService imageUploadService;

    @Transactional
    public Note noteUpload(MultipartFile multipartFile, Integer playlistId) throws IOException {
        Playlist playlist = playlistService.findById(playlistId);
        if (playlist == null) {
            throw new IllegalArgumentException("Playlist with ID " + playlistId + " not found");
        }

        String noteUrl = notesUploadService.uploadFile(multipartFile);
        Note note = new Note();
        note.setNoteUrl(noteUrl);
        note.setPlaylist(playlist);
        playlist.setNote(note);

        return noteRepository.save(note);
    }

    @Transactional
    public Note updateNote(Integer id, MultipartFile multipartFile, Integer playlistId) throws IOException {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note with ID " + id + " not found"));

        if (multipartFile != null && !multipartFile.isEmpty()) {
            String noteUrl = existingNote.getNoteUrl();
            if (noteUrl != null) {
                String notePublicId = imageUploadService.extractPublicIdFromUrl(noteUrl);
                if (notePublicId != null) {
                    imageUploadService.deleteResource(notePublicId, "raw");
                }
            }
            String newNoteUrl = notesUploadService.uploadFile(multipartFile);
            existingNote.setNoteUrl(newNoteUrl);
        }

        if (playlistId != null) {
            Playlist playlist = playlistService.findById(playlistId);
            if (playlist == null) {
                throw new IllegalArgumentException("Playlist with ID " + playlistId + " not found");
            }
            existingNote.setPlaylist(playlist);
            playlist.setNote(existingNote);
            playlistService.save(playlist);
        }

        return noteRepository.save(existingNote);
    }

    public Note getNote(Integer id) {
        return noteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note with ID " + id + " not found"));
    }

    @Transactional
    public boolean deleteNote(Integer id) throws IOException {
        Note existingNote = getNote(id);

        String noteUrl = existingNote.getNoteUrl();
        if (noteUrl != null) {
            String notePublicId = imageUploadService.extractPublicIdFromUrl(noteUrl);
            if (notePublicId != null) {
                imageUploadService.deleteResource(notePublicId, "raw");
            }
        }

        Playlist playlist = existingNote.getPlaylist();
        if (playlist != null) {
            playlist.setNote(null);
            playlistService.save(playlist);
        }
        noteRepository.delete(existingNote);
        noteRepository.flush();
        return true;
    }
}