package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.dto.TaskDTO;
import com.gangeagui.smarttasks.exception.ResourceNotFoundException;
import com.gangeagui.smarttasks.model.Task;
import com.gangeagui.smarttasks.model.Board;
import com.gangeagui.smarttasks.model.User;
import com.gangeagui.smarttasks.repository.BoardRepository;
import com.gangeagui.smarttasks.repository.TaskRepository;
import com.gangeagui.smarttasks.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(task -> {
            TaskDTO dto = new TaskDTO();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setStatus(task.getStatus());
            dto.setBoardId(task.getBoard().getId());
            dto.setAssignedToId(task.getAssignedTo().getId());
            return dto;
        }).toList();
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody @Valid TaskDTO taskDTO) {

        Board board = boardRepository.findById(taskDTO.getBoardId())
                .orElseThrow(() -> new ResourceNotFoundException("Tablero no encontrado"));

        User user = userRepository.findById(taskDTO.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setBoard(board);
        task.setAssignedTo(user);

        Task savedTask = taskRepository.save(task);

        TaskDTO responseDto = new TaskDTO();
        responseDto.setId(savedTask.getId());
        responseDto.setTitle(savedTask.getTitle());
        responseDto.setDescription(savedTask.getDescription());
        responseDto.setStatus(savedTask.getStatus());
        responseDto.setBoardId(board.getId());
        responseDto.setAssignedToId(user.getId());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody @Valid TaskDTO taskDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));

        Board board = boardRepository.findById(taskDTO.getBoardId())
                .orElseThrow(() -> new ResourceNotFoundException("Tablero no encontrado"));

        User user = userRepository.findById(taskDTO.getAssignedToId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setBoard(board);
        task.setAssignedTo(user);

        Task updatedTask = taskRepository.save(task);

        TaskDTO responseDTO = new TaskDTO();
        responseDTO.setId(updatedTask.getId());
        responseDTO.setTitle(updatedTask.getTitle());
        responseDTO.setDescription(updatedTask.getDescription());
        responseDTO.setStatus(updatedTask.getStatus());
        responseDTO.setBoardId(updatedTask.getBoard().getId());
        responseDTO.setAssignedToId(updatedTask.getAssignedTo().getId());

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskDTO> deleteTask(@PathVariable Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarea no encontrada"));

        taskRepository.delete(task);

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setBoardId(task.getBoard().getId());
        dto.setAssignedToId(task.getAssignedTo().getId());

        return ResponseEntity.ok(dto);
    }
}
