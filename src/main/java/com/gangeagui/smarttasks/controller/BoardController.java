package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.model.Board;
import com.gangeagui.smarttasks.repository.BoardRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @PostMapping
    public Board createBoard(@RequestBody @Valid Board board) {
        return boardRepository.save(board);
    }
}
