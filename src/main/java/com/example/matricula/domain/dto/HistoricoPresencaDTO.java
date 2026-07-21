package com.example.matricula.domain.dto;

import com.example.matricula.domain.entity.Presenca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPresencaDTO {
    private String id;
    private String alunoOficinaId;
    private String nomeAluno;
    private String oficinaId;
    private String nomeOficina;
    private LocalDate dataAula;
    private Presenca.StatusPresenca status;
    private String observacao;
    private LocalDateTime dataRegistro;
    private String registradoPor;
}
