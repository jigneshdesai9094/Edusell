package com.example.EducationSell.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class NotesUploadService {

    private final Cloudinary cloudinary;

    public NotesUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required and cannot be empty");
        }

        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed. Detected type: " + file.getContentType());
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds maximum limit of 10MB");
        }

        byte[] fileBytes = file.getBytes();
        String header = new String(fileBytes, 0, Math.min(fileBytes.length, 5));
        if (!header.startsWith("%PDF-")) {
            throw new IllegalArgumentException("File is not a valid PDF");
        }

        String publicId = "course_notes_" + System.currentTimeMillis();
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "raw",
                        "public_id", publicId
                ));

        String secureUrl = (String) uploadResult.get("secure_url");
        if (secureUrl == null) {
            throw new IOException("Failed to retrieve secure URL from upload result");
        }

        return secureUrl;
    }
}