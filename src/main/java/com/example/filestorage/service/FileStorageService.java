package com.example.filestorage.service;

import com.example.filestorage.model.FileMetadata;
import com.example.filestorage.repository.FileMetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileStorageService {

    private final Path rootLocation;
    private final FileMetadataRepository repository;

    public FileStorageService(@Value("${storage.location:uploads}") String storageLocation,
                              FileMetadataRepository repository) throws IOException {
        this.rootLocation = Paths.get(storageLocation);
        this.repository = repository;

        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
    }

    public FileMetadata store(MultipartFile file) throws IOException {
        Path destination = rootLocation.resolve(file.getOriginalFilename()).normalize();
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        FileMetadata metadata = new FileMetadata(
                file.getOriginalFilename(),
                file.getSize(),
                LocalDateTime.now()
        );
        return repository.save(metadata);
    }

    public byte[] load(String filename) throws IOException {
        Path path = rootLocation.resolve(filename).normalize();
        if (!Files.exists(path)) {
            throw new NoSuchFileException(filename);
        }
        return Files.readAllBytes(path);
    }

    public List<FileMetadata> listFiles() {
        return repository.findAll();
    }

    public void delete(String filename) throws IOException {
        Path filePath = rootLocation.resolve(filename).normalize();
        Files.deleteIfExists(filePath);

        repository.findAll().stream()
                .filter(f -> f.getFileName().equals(filename))
                .findFirst()
                .ifPresent(f -> repository.deleteById(f.getId()));
    }
}
