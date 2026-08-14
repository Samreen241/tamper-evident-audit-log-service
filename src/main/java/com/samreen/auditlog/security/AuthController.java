package com.samreen.auditlog.security;

import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService tokenService;
    private final Duration expiration;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenService tokenService,
                          @org.springframework.beans.factory.annotation.Value("${app.jwt.expiration:PT15M}") Duration expiration) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.expiration = expiration;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority().replaceFirst("^ROLE_", ""))
                .toList();
        return new LoginResponse(tokenService.issue(authentication.getName(), roles), "Bearer", expiration.toSeconds());
    }
}
