package com.example.matricula.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioDTO {
    
    private String id;
    
    @NotNull(message = "Dia da semana é obrigatório")
    private String diaSemana; // segunda, terca, quarta, quinta, sexta, sabado
    
    @NotBlank(message = "Hora de início é obrigatória")
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido. Use HH:MM")
    private String horaInicio;
    
    @NotBlank(message = "Hora de fim é obrigatória")
    @Pattern(regexp = "^([0-1][0-9]|2[0-3]):[0-5][0-9]$", message = "Formato de hora inválido. Use HH:MM")
    private String horaFim;
    
    @NotNull(message = "Número de vagas é obrigatório")
    @Min(value = 1, message = "Deve ter pelo menos 1 vaga")
    private Integer vagas;
}
