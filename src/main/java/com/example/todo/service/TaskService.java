/*
 * © 2025 Leandro Silveira. All rights reserved.
 */
package com.example.todo.service;

import com.example.todo.model.Task;
import com.example.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*
* This class sits between the controller and repository
* calls repository methods and can contain extra business rules if needed
 */
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return repository.findById(id);
    }

    public Task createTask(Task task) {
        return repository.save(task);
    }

    public Task updateTask(Long id, Task task) {
        task.setId(id);
        return repository.save(task);
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }

    public List<Task> getTaskByCompletion(boolean isComplete) {
        return repository.findByCompleted(isComplete);
    }

    public List<Task> getTaskByTag(String tag) {
        return repository.findByTagsContaining(tag);
    }
}
