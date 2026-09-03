package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OficinaRequestDTO {
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    private String descricao;
    
    @NotNull(message = "Categoria é obrigatória")
    private String categoria; // Categoria predefinida ou texto personalizado
    
    // Responsável
    private String responsavelId;
    
    // Horários
    @NotEmpty(message = "Pelo menos um horário deve ser informado")
    private List<HorarioDTO> horarios = new ArrayList<>();
    
    // Configurações
    private Integer idadeMinima;
    private Integer idadeMaxima;
    
    @NotNull(message = "Vagas totais é obrigatório")
    @Min(value = 1, message = "Deve ter pelo menos 1 vaga")
    private Integer vagasTotais;
    
    // Período
    @NotNull(message = "Data de início é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;
    
    @NotNull(message = "Data de fim é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataFim;
    
    // Localização
    private String local;
    private String sala;
    
    // Materiais necessários
    private List<String> materiais = new ArrayList<>();
    
    // Status
    @NotNull(message = "Status é obrigatório")
    private String status; // planejada, ativa, suspensa, encerrada
    
    private String criadoPor;
}
