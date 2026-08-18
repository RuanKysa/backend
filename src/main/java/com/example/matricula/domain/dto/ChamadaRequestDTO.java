package com.example.matricula.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChamadaRequestDTO {
    private LocalDate dataAula;
    private String horarioId;
    private List<PresencaDTO> presencas;
    private String registradoPor;
}
