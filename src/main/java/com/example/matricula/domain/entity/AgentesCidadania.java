package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agentes_cidadania")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentesCidadania {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String nome;
    
    private LocalDate dataNascimento;
    private String cpf;
    private String email;
    private String telefone;
    
    @Column(length = 500)
    private String endereco;
    
    @Column(nullable = false)
    private LocalDate dataInicio;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgente status;
    
    // Oficinas que o agente acompanha/ministra
    @ManyToMany
    @JoinTable(
        name = "agente_oficina",
        joinColumns = @JoinColumn(name = "agente_id"),
        inverseJoinColumns = @JoinColumn(name = "oficina_id")
    )
    private List<Oficina> oficinas = new ArrayList<>();
    
    public enum StatusAgente {
        ATIVO,
        INATIVO
    }
    
    // Métodos auxiliares
    public void addOficina(Oficina oficina) {
        if (!oficinas.contains(oficina)) {
            oficinas.add(oficina);
            oficina.getAgentes().add(this);  // Sincronização bidirecional
        }
    }
    
    public void removeOficina(Oficina oficina) {
        oficinas.remove(oficina);
        oficina.getAgentes().remove(this);  // Sincronização bidirecional
    }
}
