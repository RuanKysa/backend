package com.example.matricula.domain.dto;

public record LoginResponseDTO(
    boolean sucesso, String mensagem, String token, UsuarioResponseDTO usuario
) {}
