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
    
    private String matriculaId; // Nulo quando o participante for avulso
    @Column(name = "participante_avulso_id")
    private String participanteAvulsoId; // Agrupa o mesmo avulso em vários horários

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participante_avulso_id", insertable = false, updatable = false)
    private ParticipanteAvulso participanteAvulso;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private OrigemAluno origem = OrigemAluno.MATRICULA;
    
    @Column(nullable = false)
    private String nomeCompleto;
    
    private Integer idade;
    private String turno;
    private String telefone;
    private String nomeResponsavel;
    
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

    public enum OrigemAluno {
        MATRICULA,
        AVULSO
    }
}
