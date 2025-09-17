/*
 * © 2025 Leandro Silveira. All rights reserved.
 */

package com.example.filestorage.service;

import com.example.filestorage.model.FileMetadata;
import com.example.filestorage.repository.FileMetadataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileStorageServiceTest {

    @Mock FileMetadataRepository repo;

    @TempDir Path tmp;

    @Test
    void storeCreatesFileAndSavesMetadataTest() throws IOException {
        // Given
        Files.createDirectories(tmp);
        var service = new FileStorageService(tmp.toString(), repo);
        // manually ensure folder exists (we're not running Spring to call @PostConstruct)
        Files.createDirectories(Path.of(tmp.toString()));

        var mf = new MockMultipartFile("file", "notes.txt", "text/plain", "hello".getBytes());

        when(repo.save(any())).thenAnswer(inv -> {
            FileMetadata m = inv.getArgument(0);
            // pretend DB assigned id
            var saved = new FileMetadata(m.getFileName(), m.getStoredName(), m.getSize(), m.getUploadTime(), m.getContentType());
            saved.setId(1L);
            return saved;
        });

        // When
        var meta = service.store(mf);

        // Then
        assertThat(meta.getId()).isEqualTo(1L);
        assertThat(meta.getFileName()).isEqualTo("notes.txt");
        assertThat(meta.getStoredName()).contains("notes.txt");
        assertThat(Files.exists(Path.of(tmp.toString(), meta.getStoredName()))).isTrue();
        verify(repo).save(any(FileMetadata.class));
    }

    @Test
    void loadBytesReadsBackUploadedFileTest() throws IOException {
        Files.createDirectories(tmp);
        var service = new FileStorageService(tmp.toString(), repo);

        var mf = new MockMultipartFile("file", "a.txt", "text/plain", "abc".getBytes());
        when(repo.save(any())).thenAnswer(inv -> {
            FileMetadata m = inv.getArgument(0);
            m.setId(1L);
            return m;
        });

        var meta = service.store(mf);
        var bytes = service.loadBytes(meta.getStoredName());

        assertThat(new String(bytes)).isEqualTo("abc");
    }

    @Test
    void deleteRemovesFileAndDbRowTest() throws IOException {
        Files.createDirectories(tmp);
        var service = new FileStorageService(tmp.toString(), repo);

        // Seed: create a fake file and matching metadata
        String stored = "abc123_notes.txt";
        Files.writeString(Path.of(tmp.toString(), stored), "data");

        var meta = new FileMetadata("notes.txt", stored, 4L, LocalDateTime.now(), "text/plain");
        meta.setId(7L);

        when(repo.findByStoredName(stored)).thenReturn(Optional.of(meta));

        // When
        service.delete(stored);

        // Then
        assertThat(Files.exists(Path.of(tmp.toString(), stored))).isFalse();
        verify(repo).delete(meta);
    }
}
