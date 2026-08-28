package com.example.matricula.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class MatriculaResponseDTO {
    
    private String id;
    
    // DADOS PESSOAIS
    private String nomeCompleto;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    
    private Integer idade;
    private String turnoSCFV;
    private String naturalidade;
    private String municipio;
    private String uf;
    private String pais;
    
    // DOCUMENTOS
    private String rg;
    private String rgUF;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataExpedicao;
    
    private String cpf;
    
    // CAD ÚNICO (NIS)
    private String cadUnicoAluno;
    private String cadUnicoResponsavel;
    
    // ETNIA/RAÇA
    private String etnia;
    
    // PROGRAMAS SOCIAIS
    private List<String> programasSociais = new ArrayList<>();
    private String programaSocial;
    private String programaSocialOutros;
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
    private String telefone;
    private String telefoneOutro;
    
    // ESCOLA
    private String escola;
    private String serie;
    private String turno;
    
    // DEFICIÊNCIA
    private Boolean possuiDeficiencia;
    private String tipoDeficiencia;
    
    // OBSERVAÇÕES
    private String observacoes;
    
    // SAÚDE
    private Boolean usaRemediosControlados;
    private String observacaoRemedios;
    
    // AGENTE CIDADANIA
    private Boolean agentesCidadania; // Define se o aluno é um agente cidadania
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataInicioAgente; // Data de início como agente cidadania
    
    // TRANSPORTE E ALIMENTAÇÃO
    private Boolean utilizaTransporte;
    private String localEmbarque;
    private String localDesembarque;
    private Boolean almoco;
    
    // ENCAMINHAMENTO
    private String encaminhadoPor;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEncaminhamento;
    
    // Documentos Anexados
    private List<DocumentoAnexoDTO> documentos = new ArrayList<>();
    
    // Controle
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataCadastro;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataAtualizacao;
    
    private String assinatura;
    private Boolean matriculaAssinada;
}
