package com.example.matricula.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumoOficinaDTO {
    
    private Integer totalOficinas;
    private Integer oficinasAtivas;
    private Integer totalVagas;
    private Integer vagasOcupadas;
    private Integer alunosInscritos;
    private Integer responsaveisAtivos;
    private Integer agentesCidadaniaAtivos;
}
