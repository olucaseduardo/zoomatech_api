package com.credvip.credvip_mb.controller;

import com.credvip.credvip_mb.dto.user.UpdateUserRequest;
import com.credvip.credvip_mb.dto.user.UserResponse;
import com.credvip.credvip_mb.entity.User;
import com.credvip.credvip_mb.services.UserService;
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
