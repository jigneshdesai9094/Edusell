package com.example.EducationSell.Services.CloudinaryService;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final Cloudinary cloudinary;

    public Map uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required and cannot be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new IllegalArgumentException("File type cannot be determined");
        }
        String resourceType = contentType.startsWith("video") ? "video" : "image";

        return cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("resource_type", resourceType));
    }

    public void deleteResource(String publicId, String resourceType) throws IOException {
        if (publicId == null || publicId.isEmpty()) {
            throw new IllegalArgumentException("Public ID is required for deletion");
        }
        if (resourceType == null || (!resourceType.equals("image") && !resourceType.equals("video") && !resourceType.equals("raw"))) {
            throw new IllegalArgumentException("Invalid resource type: " + resourceType);
        }

        cloudinary.uploader().destroy(publicId,
                ObjectUtils.asMap("resource_type", resourceType));
    }

    public String extractPublicIdFromUrl(String url) {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("URL is required to extract public ID");
        }

        try {
            String[] parts = url.split("/upload/")[1].split("/");
            String versionAndPublicId = parts[0] + "/" + parts[1];
            String publicId = versionAndPublicId.substring(versionAndPublicId.indexOf("/") + 1);
            int extensionIndex = publicId.lastIndexOf(".");
            if (extensionIndex != -1) {
                publicId = publicId.substring(0, extensionIndex);
            }
            return publicId;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to extract public ID from URL: " + url, e);
        }
    }
}