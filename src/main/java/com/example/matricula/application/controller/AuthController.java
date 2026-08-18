package com.example.matricula.application.controller;

import com.example.matricula.domain.dto.LoginRequestDTO;
import com.example.matricula.domain.dto.LoginResponseDTO;
import com.example.matricula.domain.dto.UsuarioResponseDTO;
import com.example.matricula.domain.entity.Usuario;
import com.example.matricula.domain.repository.UsuarioRepository;
import com.example.matricula.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        Usuario usuario = repository.findByEmailIgnoreCase(request.email())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos"));
        if (usuario.getStatus() != Usuario.StatusUsuario.ATIVO ||
                !passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "E-mail ou senha incorretos");
        }
        usuario.setUltimoAcesso(LocalDateTime.now());
        repository.save(usuario);
        return new LoginResponseDTO(true, "Login realizado com sucesso", jwtService.gerar(usuario), UsuarioResponseDTO.from(usuario));
    }

    @GetMapping("/me")
    public UsuarioResponseDTO me(@AuthenticationPrincipal Usuario usuario) {
        return UsuarioResponseDTO.from(usuario);
    }
}
