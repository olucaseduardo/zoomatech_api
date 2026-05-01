package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.user.UpdateUserRequest;
import com.olucaseduardo.zoomatech_api.dto.user.UserResponse;
import com.olucaseduardo.zoomatech_api.entity.User;
import com.olucaseduardo.zoomatech_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity getCurrentUser(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(new UserResponse(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(
                userService.findAll().stream()
                        .map(UserResponse::new)
                        .toList()
        );
    }

    @PutMapping("/me")
    public ResponseEntity updateCurrentUser(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid UpdateUserRequest request) {
        User updated = userService.updateUser(user.getId(), request);
        return ResponseEntity.ok(new UserResponse(updated));
    }
}
