package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import com.example.demo.service.impl.UserServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
public class TestUser {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
}
