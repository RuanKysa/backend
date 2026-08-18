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
public class AlunoOficinaDTO {

    private String id;

    private String matriculaId;
    private String participanteAvulsoId;

    private String origem;

    @NotBlank(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    private Integer idade;
    private String turno;
    private String telefone;
    private String nomeResponsavel;
    private String observacoes;

    @NotNull(message = "Data de inscrição é obrigatória")
    private LocalDateTime dataInscricao;

    @NotBlank(message = "ID da oficina é obrigatório")
    private String oficinaId;

    @NotBlank(message = "ID do horário é obrigatório")
    private String horarioId;

    @NotNull(message = "Status é obrigatório")
    private String status;
}
