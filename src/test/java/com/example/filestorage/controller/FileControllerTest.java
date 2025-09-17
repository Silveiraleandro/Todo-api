/*
 * © 2025 Leandro Silveira. All rights reserved.
 */

package com.example.filestorage.controller;

import com.example.filestorage.model.FileMetadata;
import com.example.filestorage.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FileController.class)
class FileControllerWebMvcTest {

    @Autowired MockMvc mvc;
    @MockBean FileStorageService service;

    @Test
    void uploadReturns201AndLocationTest() throws Exception {
        var meta = new FileMetadata("a.txt", "uuid_a.txt", 3L, LocalDateTime.now(), "text/plain");
        meta.setId(10L);
        Mockito.when(service.store(any())).thenReturn(meta);

        MockMultipartFile mf = new MockMultipartFile("file", "a.txt", "text/plain", "hey".getBytes());

        mvc.perform(multipart("/api/files/upload").file(mf))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/files/uuid_a.txt")))
                .andExpect(jsonPath("$.storedName").value("uuid_a.txt"));
    }

    @Test
    void downloadReturnsBytesWithHeaderTest() throws Exception {
        var meta = new FileMetadata("a.txt", "uuid_a.txt", 3L, LocalDateTime.now(), "text/plain");
        meta.setId(10L);
        Mockito.when(service.findByStored("uuid_a.txt")).thenReturn(Optional.of(meta));
        Mockito.when(service.loadBytes("uuid_a.txt")).thenReturn("hey".getBytes());

        mvc.perform(get("/api/files/uuid_a.txt"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", containsString("filename=\"a.txt\"")))
                .andExpect(content().bytes("hey".getBytes()));
    }

    @Test
    void deleteReturns204Test() throws Exception {
        mvc.perform(delete("/api/files/uuid_a.txt"))
                .andExpect(status().isNoContent());
        Mockito.verify(service).delete("uuid_a.txt");
    }
}
