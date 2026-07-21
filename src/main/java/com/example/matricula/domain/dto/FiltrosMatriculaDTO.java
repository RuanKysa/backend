package com.example.matricula.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FiltrosMatriculaDTO {
    
    private String dataInicio; // formato: yyyy-MM-dd
    private String dataFim; // formato: yyyy-MM-dd
    private String busca; // busca por nome, cpf, etc.
    private String status; // filtro por status
}
