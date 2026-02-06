package com.credvip.credvip_mb.services;

import com.credvip.credvip_mb.dto.auth.RegisterRequest;
import com.credvip.credvip_mb.dto.user.UpdateUserRequest;
import com.credvip.credvip_mb.entity.Role;
import com.credvip.credvip_mb.entity.User;
import com.credvip.credvip_mb.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User createUser(@Valid RegisterRequest request) {

        var newUser = new User();
        newUser.setEmail(request.email());
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(Role.ADMIN);

        return userRepository.save(newUser);
    }

    public User updateUser(UUID id, @Valid UpdateUserRequest request) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            return null;
        }

        user.get().setFirstName(request.firstName());
        user.get().setLastName(request.lastName());
        user.get().setPassword(new BCryptPasswordEncoder().encode(request.password()));

        return userRepository.save(user.get());
    }

    public Optional<User> findByEmail(@NotBlank String email) {
        return userRepository.findByEmail(email);
    }
}
