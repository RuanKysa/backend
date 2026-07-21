package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatriculaRequestDTO {
    
    // DADOS PESSOAIS
    @NotBlank(message = "Nome completo é obrigatório")
    @Size(min = 3, max = 200, message = "Nome deve ter entre 3 e 200 caracteres")
    private String nomeCompleto;
    
    @NotNull(message = "Data de nascimento é obrigatória")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    private Integer idade;
    
    private String turnoSCFV; // manha, tarde, integral
    
    private String naturalidade;
    private String municipio;
    private String uf;
    private String pais;
    
    // DOCUMENTOS
    private String rg;
    private String rgUF;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExpedicao;
    
    @Pattern(regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "CPF inválido")
    private String cpf;
    
    // CAD ÚNICO (NIS)
    private String cadUnicoAluno;
    private String cadUnicoResponsavel;
    
    // ETNIA/RAÇA
    private String etnia; // branca, negra, parda, amarela, indigena, nao_informar
    
    // PROGRAMAS SOCIAIS
    private String programaSocial; // nao_possui, bolsa_familia, bpc, tarifa_social
    
    @Min(value = 1, message = "Número de pessoas na residência deve ser no mínimo 1")
    private Integer quantasPessoasResidencia;
    
    // FILIAÇÃO
    private String nomeMae;
    private String rgMae;
    private String ufMae;
    private String nomePai;
    private String rgPai;
    private String ufPai;
    
    // RESPONSÁVEL
    private String nomeResponsavel;
    private String parentesco;
    
    // ENDEREÇO
    private String endereco;
    private String numeroEndereco;
    private String complemento;
    private String bairro;
    private String postoDeSaude;
    
    // CONTATO
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}-?\\d{4}$", message = "Telefone inválido")
    private String telefone;
    
    private String telefoneOutro;
    
    // ESCOLA
    private String escola;
    private String serie;
    private String turno; // manha, tarde, noite
    
    // DEFICIÊNCIA
    @NotNull(message = "Informar se possui deficiência é obrigatório")
    private Boolean possuiDeficiencia;
    
    private String tipoDeficiencia; // fisica, auditiva, visual, mental, nao_possui
    
    // OBSERVAÇÕES
    @Size(max = 2000, message = "Observações não podem exceder 2000 caracteres")
    private String observacoes;
    
    // SAÚDE
    private Boolean usaRemediosControlados;
    
    @Size(max = 1000, message = "Observação sobre remédios não pode exceder 1000 caracteres")
    private String observacaoRemedios;
    
    // AGENTE CIDADANIA
    private Boolean agentesCidadania; // Define se o aluno é um agente cidadania
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicioAgente; // Data de início como agente cidadania
    
    // TRANSPORTE E ALIMENTAÇÃO
    @NotNull(message = "Informar se utiliza transporte é obrigatório")
    private Boolean utilizaTransporte;
    
    private String localEmbarque;
    private String localDesembarque;
    
    @NotNull(message = "Informar sobre almoço é obrigatório")
    private Boolean almoco;
    
    // ENCAMINHAMENTO
    private String encaminhadoPor;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEncaminhamento;
    
    // Documentos Anexados
    private List<DocumentoAnexoDTO> documentos = new ArrayList<>();
    
    // Controle
    private String status; // pendente, aprovada, rejeitada, aguardando_documentos
    private String assinatura;
    private Boolean matriculaAssinada;
}
