package com.example.matricula.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosOficinaDTO {
    
    private String categoria;
    private String status;
    private String responsavel;
    private String diaSemana;
    private String dataInicio;
    private String dataFim;
    private String pesquisa; // busca por nome ou descrição
}
