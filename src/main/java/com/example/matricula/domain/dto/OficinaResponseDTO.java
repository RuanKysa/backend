package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OficinaResponseDTO {
    
    private String id;
    private String nome;
    private String descricao;
    private String categoria;
    
    // Responsável
    private ResponsavelDTO responsavel;
    private String responsavelId;
    
    // Horários
    private List<HorarioDTO> horarios = new ArrayList<>();
    
    // Configurações
    private Integer idadeMinima;
    private Integer idadeMaxima;
    private Integer vagasTotais;
    private Integer vagasDisponiveis;
    
    // Período
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;
    
    // Localização
    private String local;
    private String sala;
    
    // Materiais necessários
    private List<String> materiais = new ArrayList<>();
    
    // Status
    private String status;
    
    // Controle
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCriacao;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    
    private String criadoPor;
}
