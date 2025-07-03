package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.dto.BoardDTO;
import com.gangeagui.smarttasks.exception.ResourceNotFoundException;
import com.gangeagui.smarttasks.model.Board;
import com.gangeagui.smarttasks.repository.BoardRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardRepository.findAll().stream().map(board -> {
            BoardDTO dto =  new BoardDTO();
            dto.setId(board.getId());
            dto.setName(board.getName());
            return dto;
        }).toList();
    }

    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(@RequestBody @Valid BoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());

        Board savedBoard = boardRepository.save(board);

        BoardDTO responseDto = new BoardDTO();
        responseDto.setId(savedBoard.getId());
        responseDto.setName(savedBoard.getName());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @RequestBody @Valid BoardDTO boardDTO) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tablero no encontrado"));

        board.setName(boardDTO.getName());
        Board updatedBoard = boardRepository.save(board);

        BoardDTO responseDto = new BoardDTO();
        responseDto.setId(updatedBoard.getId());
        responseDto.setName(updatedBoard.getName());

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BoardDTO> deleteBoard(@PathVariable Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tablero no encontrado"));

        boardRepository.delete(board);

        BoardDTO dto = new BoardDTO();
        dto.setId(board.getId());
        dto.setName(board.getName());

        return ResponseEntity.ok(dto);
    }
}
