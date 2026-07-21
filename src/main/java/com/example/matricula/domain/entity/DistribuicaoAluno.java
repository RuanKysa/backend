package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "distribuicao_alunos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistribuicaoAluno {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String alunoId;
    
    @Column(nullable = false)
    private String oficinaId;
    
    @Column(nullable = false)
    private String horarioId;
    
    @Column(nullable = false)
    private LocalDateTime dataInscricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDistribuicao status;
    
    @Column(length = 1000)
    private String observacoes;
    
    public enum StatusDistribuicao {
        CONFIRMADO,
        PENDENTE,
        CANCELADO
    }
}
