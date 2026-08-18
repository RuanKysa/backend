package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "aulas", uniqueConstraints = @UniqueConstraint(columnNames = {"oficina_id", "horario_id", "data_aula"}))
@Data
@NoArgsConstructor
public class Aula {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false) private String oficinaId;
    @Column(nullable = false) private String horarioId;
    @Column(nullable = false) private LocalDate dataAula;
    private String tema;
    @Column(length = 1000) private String observacoes;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private StatusAula status = StatusAula.REALIZADA;
    private String criadoPor;
    @Column(nullable = false, updatable = false) private LocalDateTime dataCriacao;
    @PrePersist void criar() { if (dataCriacao == null) dataCriacao = LocalDateTime.now(); }
    public enum StatusAula { PLANEJADA, REALIZADA, CANCELADA }
}
