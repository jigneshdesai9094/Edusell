package com.example.EducationSell.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class NotesUploadService {

    @Autowired
    private final Cloudinary cloudinary;

    public NotesUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) {
        if (!file.getContentType().equals("application/pdf")) {
            throw new RuntimeException("Only PDF files are allowed. Detected type: " + file.getContentType());
        }
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new RuntimeException("File size exceeds maximum limit of 10MB");
        }
        try {
            byte[] fileBytes = file.getBytes();
            String header = new String(fileBytes, 0, Math.min(fileBytes.length, 5));
            if (!header.startsWith("%PDF-")) {
                throw new RuntimeException("File is not a valid PDF");
            }

            String publicId = "course_notes_" + System.currentTimeMillis();
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "raw",
                            "public_id", publicId
                    ));
            System.out.println("Cloudinary Upload Result: " + uploadResult);

            String secureUrl = (String) uploadResult.get("secure_url");
            if (secureUrl == null) {
                throw new RuntimeException("Failed to retrieve secure URL from upload result");
            }

            // Optional: Use a simpler transformation or none
            // secureUrl = secureUrl.replace("/upload/", "/upload/f_pdf/"); // Use if needed
            return secureUrl;
        } catch (IOException e) {
            throw new RuntimeException("Could not upload file to Cloudinary: " + e.getMessage(), e);
        }
    }
}