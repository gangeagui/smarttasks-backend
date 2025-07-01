package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.model.Task;
import com.gangeagui.smarttasks.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    public Task createTask(@RequestBody @Valid Task task) {
        return taskRepository.save(task);
    }
}
