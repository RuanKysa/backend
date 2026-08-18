package com.example.matricula.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres") String senha,
    @NotBlank String perfil,
    @NotBlank String status
) {}
