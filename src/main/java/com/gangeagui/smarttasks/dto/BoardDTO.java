package com.gangeagui.smarttasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BoardDTO {

    private Long id;

    @NotBlank(message = "El nombre del tablero es obligatorio")
    @Size(min = 3, max = 100)
    private String name;

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
}
