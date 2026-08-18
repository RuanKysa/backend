package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@Data
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 160)
    private String nome;

    @Column(nullable = false, length = 180)
    private String email;

    @Column(nullable = false, name = "senha_hash")
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Perfil perfil = Perfil.USUARIO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusUsuario status = StatusUsuario.ATIVO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    private LocalDateTime ultimoAcesso;

    @PrePersist
    void prePersist() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
        email = email.trim().toLowerCase();
    }

    @PreUpdate
    void preUpdate() {
        email = email.trim().toLowerCase();
    }

    public enum Perfil { ADMIN, USUARIO, VISITANTE }
    public enum StatusUsuario { ATIVO, INATIVO, BLOQUEADO }
}
