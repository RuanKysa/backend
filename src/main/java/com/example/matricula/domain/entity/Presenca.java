package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "presencas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aluno_oficina_id", "aula_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Presenca {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(name = "aluno_oficina_id", nullable = false)
    private String alunoOficinaId; // Referência ao AlunoOficina
    
    @Column(name = "oficina_id", nullable = false)
    private String oficinaId;

    @Column(name = "horario_id", nullable = false)
    private String horarioId;

    @Column(name = "aula_id", nullable = false)
    private String aulaId;
    
    @Column(name = "data_aula", nullable = false)
    private LocalDate dataAula;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPresenca status;
    
    @Column(length = 500)
    private String observacao;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataRegistro;
    
    private String registradoPor; // Pode ser o nome do professor/responsável
    
    @PrePersist
    protected void onCreate() {
        if (dataRegistro == null) {
            dataRegistro = LocalDateTime.now();
        }
    }
    
    public enum StatusPresenca {
        PRESENTE,
        FALTA,
        JUSTIFICADA
    }
}
