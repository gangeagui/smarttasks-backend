package com.gangeagui.smarttasks.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del tablero es obligatorio")
    @Size(min = 3, max = 100, message = "El tablero debe tener entre 3 y 100 caracteres")
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public Board() {}

    public Board(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
