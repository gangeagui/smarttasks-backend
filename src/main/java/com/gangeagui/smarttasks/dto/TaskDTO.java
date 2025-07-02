package com.gangeagui.smarttasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TaskDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(min = 3, max = 100)
    private String title;

    private String description;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    @NotNull(message = "Debe asignar esta tarea a un tablero")
    private Long boardId;

    @NotNull(message = "Debe asignar esta tarea a un usuario")
    private Long assignedToId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public Long getAssignedToId() {
        return assignedToId;
    }

    public void setAssignedToId(Long assignedToId) {
        this.assignedToId = assignedToId;
    }
}
