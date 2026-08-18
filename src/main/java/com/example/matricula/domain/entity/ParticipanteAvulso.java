package com.example.matricula.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "participantes_avulsos")
@Data
@NoArgsConstructor
public class ParticipanteAvulso {
    @Id
    private String id;

    @Column(nullable = false)
    private String nomeCompleto;
    private Integer idade;
    private String turno;
    private String telefone;
    private String nomeResponsavel;

    @Column(length = 1000)
    private String observacoes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    @PrePersist
    void criar() {
        if (dataCriacao == null) dataCriacao = LocalDateTime.now();
    }

    @PreUpdate
    void atualizar() {
        dataAtualizacao = LocalDateTime.now();
    }
}
