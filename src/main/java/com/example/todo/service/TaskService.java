package com.example.todo.service;

import com.example.todo.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById() {
        return repository.findbyId();
    }

    public Task createTask() {
        return repository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        task.setId(id)
        return repository.save(task);
    }

    public Task deleteTask(Long id) {
        return repository.delete(task);
    }

    public List<Task> getTaskByCompletion(boolean isComplete) {
        return repository.findByCompletion(isComplete);
    }

    public List<Task> getTaskByTag(String tag) {
        return repository.findByTagContaining(tag);
    }
}
