package com.ggiiig.user.service;

import com.ggiiig.user.entity.User;
import com.ggiiig.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User login(String id, String password) {
        return userRepository.findByIdAndPassword(id, password).orElse(null);
    }
}
