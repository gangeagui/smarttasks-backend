package com.gangeagui.smarttasks.controller;

import com.gangeagui.smarttasks.dto.UserDTO;
import com.gangeagui.smarttasks.model.User;
import com.gangeagui.smarttasks.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            return dto;
        }).toList();
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

        UserDTO responseDto = new UserDTO();
        responseDto.setId(savedUser.getId());
        responseDto.setUsername(savedUser.getUsername());
        responseDto.setEmail(savedUser.getEmail());

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
}
