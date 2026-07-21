package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "responsaveis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Responsavel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String nome;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoResponsavel tipo;
    
    private String email;
    private String telefone;
    private String especialidade;
    private LocalDate dataAdmissao;
    
    @Column(columnDefinition = "TEXT")
    private String anexoCriminal;
    
    public enum TipoResponsavel {
        PROFESSOR,
        ESTAGIARIO
    }
}
