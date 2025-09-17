/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.todo.service;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock TaskRepository repository;
    @InjectMocks TaskService service;

    @Captor ArgumentCaptor<Task> taskCaptor;

    @BeforeEach
    void setup() {}

    @Test
    void getAllTasksCallsRepoTest() {
        when(repository.findAll()).thenReturn(List.of());
        var tasks = service.getAllTasks();
        verify(repository, times(1)).findAll();
        assertThat(tasks).isEmpty();
    }

    @Test
    void createTaskSavesAndReturnsTest() {
        var toSave = Task.builder().title("Test").description("d").build();
        var saved  = Task.builder().id(1L).title("Test").description("d").build();
        when(repository.save(any(Task.class))).thenReturn(saved);

        var result = service.createTask(toSave);

        verify(repository).save(taskCaptor.capture());
        assertThat(taskCaptor.getValue().getTitle()).isEqualTo("Test");
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void updateTaskUpdatesExistingTest() {
        when(repository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = Task.builder().title("New").completed(true).build();
        var out = service.updateTask(5L, patch);

        // verify the call and capture argument
        verify(repository).save(taskCaptor.capture());
        Task saved = taskCaptor.getValue();

        // assert the values
        assertThat(saved.getId()).isEqualTo(5L);
        assertThat(saved.getTitle()).isEqualTo("New");
        assertThat(saved.isCompleted()).isTrue();

        // also assert method return
        assertThat(out.getTitle()).isEqualTo("New");
        assertThat(out.isCompleted()).isTrue();
    }

    @Test
    void deleteTaskDeletesByIdTest() {
        service.deleteTask(3L);
        verify(repository).deleteById(3L);
    }
}
