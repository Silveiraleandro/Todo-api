package com.example.todo.service;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    @Mock
    TaskRepository repository;

    @InjectMocks
    TaskService taskService;

    public TaskServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTasks() {
        taskService.getAllTasks();
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    void testCreateTask() {
        Task task = Task.builder().title("Test").build();
        taskService.createTask(task);
        Mockito.verify(repository, Mockito.times(1)).save(task);
    }
}