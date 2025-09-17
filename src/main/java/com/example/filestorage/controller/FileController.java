/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.filestorage.controller;

import com.example.filestorage.model.FileMetadata;
import com.example.filestorage.service.FileStorageService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileStorageService storageService;

    public FileController(FileStorageService storageService) {

        this.storageService = storageService;
    }

    // UPLOAD
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileMetadata> upload(@RequestPart("file") MultipartFile file) throws IOException {
        FileMetadata saved = storageService.store(file);
        return ResponseEntity
                .created(java.net.URI.create("/api/files/" + saved.getStoredName()))
                .body(saved);
    }

    // LIST
    @GetMapping
    public List<FileMetadata> list() {
        return storageService.listFiles();
    }

    // DOWNLOAD
    @GetMapping("/{storedName}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable String storedName) throws IOException {
        var meta = storageService.findByStored(storedName)
                .orElseThrow(() -> new NoSuchFileException(storedName));

            byte[] data = storageService.loadBytes(storedName);
            var resource = new ByteArrayResource(data);

            String contentType = (meta.getContentType() != null && !meta.getContentType().isBlank())
                    ? meta.getContentType()
                    : MediaType.APPLICATION_OCTET_STREAM_VALUE;

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + meta.getFileName() + "\"")
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(meta.getSize())
                    .body(resource);
    }

    // DELETE
    @DeleteMapping("/{storedName}")
    public ResponseEntity<Void> delete(@PathVariable String storedName) throws IOException {
            storageService.delete(storedName);
            return ResponseEntity.noContent().build();
    }
}
