package com.example.matricula.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelatorioFrequenciaDTO {
    private String alunoOficinaId;
    private String nomeAluno;
    private long totalAulas;
    private long presencas;
    private long faltas;
    private long justificadas;
    private double percentualPresenca;
}
