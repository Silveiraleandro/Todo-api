package com.example.todo.controller;

import com.example.todo.model.Task;
import com.example.todo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping
    public List<Task> getAllTasks(@RequestParam(required = false) Boolean completed,
                                  @RequestParam(required = false) String tag) {
        if (completed != null) return service.getTaskByCompletion(completed);
        if (tag != null) return service.getTaskByTag(tag);
        return service.getAllTasks();
    }

    @GetMapping("/(id)")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        return service.getTaskById()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return service.createTask();
    }

    @PutMapping
    public Task updateTask(@PathVariable Long id, @RequestBody Task task) {
        return service.updateTask(id, task);
    }

    @DeleteMapping
    public void deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
