package com.chatop.api.service;

import com.chatop.api.config.AppProperties;
import com.chatop.api.exception.FileStorageException;
import com.chatop.api.exception.InvalidFileException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.UUID;

@Service
public class FileStorageService {

  private static final String UPLOAD_PATH = "/uploads/";

  private final Path uploadDir;
  private final String baseUrl;

  public FileStorageService(AppProperties appProperties) {
    this.uploadDir = Path.of(appProperties.uploadDir()).toAbsolutePath().normalize();
    this.baseUrl = appProperties.baseUrl();
  }

  public String store(MultipartFile file) {
    if (file.isEmpty()) {
      throw new InvalidFileException("Picture is empty");
    }
    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new InvalidFileException("Picture must be an image");
    }

    String fileName = UUID.randomUUID() + extractExtension(file.getOriginalFilename());
    try {
      Files.createDirectories(uploadDir);
      file.transferTo(uploadDir.resolve(fileName));
    } catch (IOException exception) {
      throw new FileStorageException("Unable to store picture", exception);
    }
    return baseUrl + UPLOAD_PATH + fileName;
  }

  private String extractExtension(String originalFileName) {
    if (originalFileName == null || !originalFileName.contains(".")) {
      return "";
    }
    String extension = originalFileName.substring(originalFileName.lastIndexOf('.')).toLowerCase(Locale.ROOT);
    return extension.matches("\\.[a-z0-9]{1,5}") ? extension : "";
  }
}
