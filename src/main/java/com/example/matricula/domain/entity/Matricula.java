package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "matriculas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    // DADOS PESSOAIS
    @Column(nullable = false)
    private String nomeCompleto;
    
    @Column(nullable = false)
    private LocalDate dataNascimento;
    
    private Integer idade;
    
    @Enumerated(EnumType.STRING)
    private TurnoSCFV turnoSCFV;
    
    private String naturalidade;
    private String municipio;
    private String uf;
    private String pais;
    
    // DOCUMENTOS
    private String rg;
    private String rgUF;
    private LocalDate dataExpedicao;
    private String cpf;
    
    // CAD ÚNICO (NIS)
    private String cadUnicoAluno;
    private String cadUnicoResponsavel;
    
    // ETNIA/RAÇA
    @Enumerated(EnumType.STRING)
    private Etnia etnia;
    
    // PROGRAMAS SOCIAIS
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "matricula_programas_sociais", joinColumns = @JoinColumn(name = "matricula_id"))
    @Column(name = "programa_social", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<ProgramaSocial> programasSociais = new LinkedHashSet<>();

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
    
    @Enumerated(EnumType.STRING)
    private TurnoEscola turno;
    
    // DEFICIÊNCIA
    @Column(nullable = false)
    private Boolean possuiDeficiencia = false;
    
    @Enumerated(EnumType.STRING)
    private TipoDeficiencia tipoDeficiencia;
    
    // OBSERVAÇÕES
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    // SAÚDE
    private Boolean usaRemediosControlados;
    
    @Column(columnDefinition = "TEXT")
    private String observacaoRemedios;
    
    // AGENTE CIDADANIA
    private Boolean agentesCidadania; // Define se o aluno é um agente cidadania
    private LocalDate dataInicioAgente; // Data de início como agente cidadania
    
    // TRANSPORTE E ALIMENTAÇÃO
    @Column(nullable = false)
    private Boolean utilizaTransporte = false;
    
    private String localEmbarque;
    private String localDesembarque;
    
    @Column(nullable = false)
    private Boolean almoco = false;
    
    // ENCAMINHAMENTO
    private String encaminhadoPor;
    private LocalDate dataEncaminhamento;
    
    // Documentos Anexados
    @OneToMany(mappedBy = "matricula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoAnexo> documentos = new ArrayList<>();
    
    // Controle
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMatricula status = StatusMatricula.PENDENTE;
    
    @Column(nullable = false)
    private LocalDateTime dataCadastro;
    
    private LocalDateTime dataAtualizacao;
    
    @Column(columnDefinition = "TEXT")
    private String assinatura;
    
    @Column(nullable = false)
    private Boolean matriculaAssinada = false;
    
    // Enumerações
    public enum TurnoSCFV {
        MANHA, TARDE, INTEGRAL
    }
    
    public enum Etnia {
        BRANCA, NEGRA, PARDA, AMARELA, INDIGENA, NAO_INFORMAR
    }
    
    public enum ProgramaSocial {
        NAO_POSSUI, BOLSA_FAMILIA, BPC, TARIFA_SOCIAL, AUXILIO_GAS, PETI, RENDA_CIDADA, CESTA_BASICA, OUTROS
    }
    
    public enum TurnoEscola {
        MANHA, TARDE, NOITE
    }
    
    public enum TipoDeficiencia {
        FISICA, AUDITIVA, VISUAL, MENTAL, NAO_POSSUI
    }
    
    public enum StatusMatricula {
        PENDENTE, APROVADA, REJEITADA, AGUARDANDO_DOCUMENTOS
    }
    
    // Métodos auxiliares
    public void addDocumento(DocumentoAnexo documento) {
        documentos.add(documento);
        documento.setMatricula(this);
    }
    
    public void removeDocumento(DocumentoAnexo documento) {
        documentos.remove(documento);
        documento.setMatricula(null);
    }
    
    @PrePersist
    protected void onCreate() {
        dataCadastro = LocalDateTime.now();
        if (status == null) {
            status = StatusMatricula.PENDENTE;
        }
        if (possuiDeficiencia == null) {
            possuiDeficiencia = false;
        }
        if (utilizaTransporte == null) {
            utilizaTransporte = false;
        }
        if (almoco == null) {
            almoco = false;
        }
        if (matriculaAssinada == null) {
            matriculaAssinada = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        dataAtualizacao = LocalDateTime.now();
    }
}
