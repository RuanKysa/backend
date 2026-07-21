package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponsavelDTO {
    
    private String id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @NotNull(message = "Tipo é obrigatório")
    private String tipo;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String telefone;
    private String especialidade;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataAdmissao;
    
    private String anexoCriminal;
}
