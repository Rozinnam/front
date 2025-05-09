package com.example.front.user.service;

import com.example.front.user.entity.User;
import com.example.front.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User login(String id, String password) {
        return userRepository.findByIdAndPassword(id, password).orElse(null);
    }
}
