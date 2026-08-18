package com.example.matricula.domain.dto;

import com.example.matricula.domain.entity.Usuario;
import java.time.LocalDateTime;

public record UsuarioResponseDTO(
    String id, String nome, String email, String perfil, String status,
    LocalDateTime dataCriacao, LocalDateTime ultimoAcesso
) {
    public static UsuarioResponseDTO from(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(), usuario.getNome(), usuario.getEmail(),
            usuario.getPerfil().name().toLowerCase(), usuario.getStatus().name().toLowerCase(),
            usuario.getDataCriacao(), usuario.getUltimoAcesso()
        );
    }
}
