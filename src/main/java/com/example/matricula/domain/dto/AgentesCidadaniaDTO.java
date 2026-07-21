package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentesCidadaniaDTO {
    
    private String id;
    
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    private String cpf;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String telefone;
    private String endereco;
    
    @NotNull(message = "Data de início é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicio;
    
    @NotNull(message = "Status é obrigatório")
    private String status; // ativo, inativo
    
    // Lista de IDs das oficinas vinculadas (relacionamento ManyToMany via tabela agente_oficina)
    private List<String> oficinasIds = new ArrayList<>();
}
