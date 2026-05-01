package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.auth.AuthResponse;
import com.olucaseduardo.zoomatech_api.dto.auth.LoginRequest;
import com.olucaseduardo.zoomatech_api.dto.auth.RefreshRequest;
import com.olucaseduardo.zoomatech_api.dto.auth.RegisterRequest;
import com.olucaseduardo.zoomatech_api.entity.User;
import com.olucaseduardo.zoomatech_api.exceptions.InvalidTokenException;
import com.olucaseduardo.zoomatech_api.services.JwtService;
import com.olucaseduardo.zoomatech_api.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterRequest request) {
        User user = userService.createUser(request);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = (User) auth.getPrincipal();
        System.out.println(user);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);


        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtService.extractUsername(refreshToken);

        User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException("Invalid refresh token");
        }

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }
}

