package com.example.demo.controller;

import com.example.demo.dto.ResponseData;
import com.example.demo.dto.request.UserRequestDto;
import com.example.demo.model.User;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/user")
public class UserResrController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping
    public ResponseData<?> getAllUser() {
        return null;
    }

    @PostMapping
    public ResponseData<?> create(@RequestBody UserRequestDto userRequestDto) {
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), "create success", userService.saveUse(userRequestDto));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add user fail");
        }
    }

    @GetMapping("/{id}")
    public ResponseData<?> getUserById(@PathVariable("id") long id) {
        return new ResponseData<>(HttpStatus.ACCEPTED.value(), "success", userService.getUser(id));
    }

    @PutMapping("/{id}")
    public ResponseData<?> update(@RequestBody UserRequestDto userRequestDto) {
        return null;
    }
}
