package com.example.matricula.domain.dto;

import com.example.matricula.domain.entity.Presenca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresencaDTO {
    private String alunoOficinaId;
    private Presenca.StatusPresenca status;
    private String observacao;
}
