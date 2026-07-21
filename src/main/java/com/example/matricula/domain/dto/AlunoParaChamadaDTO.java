package com.example.matricula.domain.dto;

import com.example.matricula.domain.entity.Presenca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoParaChamadaDTO {
    private String alunoOficinaId;
    private String nomeCompleto;
    private Integer idade;
    private String turno;
    private Presenca.StatusPresenca statusPresenca; // Status se já tiver chamada registrada na data
}
