package com.example.todo.repository;

import com.example.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* An interface that extends JpaRepository for data access layer
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByCompleted(boolean completed);
    List<Task> findByTagsContaining(String tag);
}
