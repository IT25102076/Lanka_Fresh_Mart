package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.dto.UserDto;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .role(userDto.getRole())
                .build();

        return userRepository.save(user);
    }
}
