/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.filestorage.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // original name as uploaded by user (for display/download filename)
    @Column(nullable = false)
    private String fileName;
    // Collision-safe name used on disk (UUID_prefix), Must be unique
    @Column(nullable = false, unique = true)
    private String storedName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private LocalDateTime uploadTime;
    // Optional
    private String contentType;

    public FileMetadata() {}

    public FileMetadata(String filename, String storedName, Long size, LocalDateTime uploadTime, String contentType) {
        this.fileName = filename;
        this.storedName = storedName;
        this.size = size;
        this.uploadTime = uploadTime;
        this.contentType = contentType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
