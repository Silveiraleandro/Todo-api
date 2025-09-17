/*
 * © 2025 Leandro Silveira. All rights reserved.
 */

package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerWebMvcTest {

    @Autowired MockMvc mvc;
    @MockBean TaskService taskService;

    @Test
    void listReturnsOkAndArrayTest() throws Exception {
        Mockito.when(taskService.getAllTasks())
                .thenReturn(List.of(Task.builder().id(1L).title("T1").build()));

        mvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("T1")));
    }

    @Test
    void createReturnsCreatedTest() throws Exception {
        Mockito.when(taskService.createTask(Mockito.any()))
                .thenReturn(Task.builder().id(2L).title("New").build());

        mvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/tasks/")))
                .andExpect(jsonPath("$.id", is(2)));
    }
}
