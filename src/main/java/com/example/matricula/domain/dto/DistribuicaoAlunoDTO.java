package com.example.matricula.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistribuicaoAlunoDTO {
    
    private String id;
    
    @NotBlank(message = "ID do aluno é obrigatório")
    private String alunoId;
    
    @NotBlank(message = "ID da oficina é obrigatório")
    private String oficinaId;
    
    @NotBlank(message = "ID do horário é obrigatório")
    private String horarioId;
    
    @NotNull(message = "Data de inscrição é obrigatória")
    private LocalDateTime dataInscricao;
    
    @NotNull(message = "Status é obrigatório")
    private String status; // confirmado, pendente, cancelado
    
    private String observacoes;
}
