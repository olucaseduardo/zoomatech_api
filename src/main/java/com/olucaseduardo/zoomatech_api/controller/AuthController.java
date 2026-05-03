package com.olucaseduardo.zoomatech_api.controller;

import com.olucaseduardo.zoomatech_api.dto.ApiResponse;
import com.olucaseduardo.zoomatech_api.dto.auth.AuthResponse;
import com.olucaseduardo.zoomatech_api.dto.auth.LoginRequest;
import com.olucaseduardo.zoomatech_api.dto.auth.RefreshRequest;
import com.olucaseduardo.zoomatech_api.dto.auth.RegisterRequest;
import com.olucaseduardo.zoomatech_api.entity.User;
import com.olucaseduardo.zoomatech_api.exceptions.InvalidTokenException;
import com.olucaseduardo.zoomatech_api.exceptions.ResourceNotFoundException;
import com.olucaseduardo.zoomatech_api.services.JwtService;
import com.olucaseduardo.zoomatech_api.services.UserService;
import com.olucaseduardo.zoomatech_api.util.ResponseUtil;
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
    public ResponseEntity<ApiResponse<AuthResponse>> register(@RequestBody @Valid RegisterRequest request) {
        User user = userService.createUser(request);

        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return ResponseEntity.ok(ResponseUtil.success("Usuário adicionado com sucesso", new AuthResponse(accessToken, refreshToken), null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody @Valid LoginRequest request) {
        this.userService.findByEmail(request.email()).orElseThrow(
                () -> new ResourceNotFoundException("Usuário", request.email())
        );

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


        return ResponseEntity.ok(ResponseUtil.success("Acesso autorizado com sucesso!", new AuthResponse(accessToken, refreshToken), null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtService.extractUsername(refreshToken);

        User user = userService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new InvalidTokenException("O refresh token é inválido.");
        }

        String accessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(ResponseUtil.success("Token atualizado!", new AuthResponse(accessToken, refreshToken), null));
    }
}

