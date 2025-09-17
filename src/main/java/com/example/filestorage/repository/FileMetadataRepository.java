/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.filestorage.repository;

import com.example.filestorage.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    Optional<FileMetadata> findByStoredName(String storedName);
    Optional<FileMetadata> findByFileName(String fileName); // optinal helper
    void deleteByStoredName(String storedName);
}
