package com.notekeeper.Notemind.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.notekeeper.Notemind.model.User;
import com.notekeeper.Notemind.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "Username already exists!";
        }
        userRepository.save(user);
        return "User registered successfully!";
    }

    public String loginUser(User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser == null) {
            return "User not found!";
        }
        if (!existingUser.getPassword().equals(user.getPassword())) {
            return "Invalid password!";
        }
        return "Login successful!";
    }
}
