package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oficinas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oficina {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(length = 2000)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaOficina categoria;
    
    // Responsável
    @ManyToOne
    @JoinColumn(name = "responsavel_id")
    private Responsavel responsavel;
    
    // Horários
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "oficina_id")
    private List<Horario> horarios = new ArrayList<>();
    
    // Configurações
    private Integer idadeMinima;
    private Integer idadeMaxima;
    
    @Column(nullable = false)
    private Integer vagasTotais;
    
    @Column(nullable = false)
    private Integer vagasDisponiveis;
    
    // Período
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    @Column(nullable = false)
    private LocalDate dataFim;
    
    // Localização
    private String local;
    private String sala;
    
    // Materiais necessários
    @ElementCollection
    @CollectionTable(name = "oficina_materiais", joinColumns = @JoinColumn(name = "oficina_id"))
    @Column(name = "material")
    private List<String> materiais = new ArrayList<>();
    
    // Agentes de cidadania vinculados
    @ManyToMany(mappedBy = "oficinas")
    private List<AgentesCidadania> agentes = new ArrayList<>();
    
    // Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOficina status;
    
    // Controle
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    private LocalDateTime dataAtualizacao;
    
    private String criadoPor;
    
    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
    
    public enum CategoriaOficina {
        ESPORTE,
        ARTE,
        MUSICA,
        DANCA,
        ARTESANATO,
        INFORMATICA,
        IDIOMAS,
        OUTRAS
    }
    
    public enum StatusOficina {
        PLANEJADA,
        ATIVA,
        SUSPENSA,
        ENCERRADA
    }
}
