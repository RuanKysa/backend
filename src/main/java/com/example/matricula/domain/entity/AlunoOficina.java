package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alunos_oficina")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoOficina {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String matriculaId; // Referência à matrícula do aluno
    
    @Column(nullable = false)
    private String nomeCompleto;
    
    private Integer idade;
    private String turno;
    
    @Column(length = 1000)
    private String observacoes;
    
    @Column(nullable = false)
    private LocalDateTime dataInscricao;
    
    // Campos de relacionamento com oficina e horário
    @Column(nullable = false, length = 255)
    private String oficinaId;
    
    @Column(nullable = false, length = 255)
    private String horarioId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusInscricao status = StatusInscricao.CONFIRMADO;
    
    public enum StatusInscricao {
        CONFIRMADO,
        PENDENTE,
        CANCELADO
    }
}
