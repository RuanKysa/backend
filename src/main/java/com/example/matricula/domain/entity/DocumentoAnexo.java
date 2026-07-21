package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentos_anexo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAnexo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDocumento tipo;
    
    @Column(nullable = false)
    private String nomeArquivo;
    
    @Column(columnDefinition = "TEXT")
    private String url;
    
    @Column(nullable = false)
    private LocalDateTime dataUpload;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;
    
    public enum TipoDocumento {
        RG("rg"),
        CPF("cpf"),
        COMPROVANTE_RESIDENCIA("comprovante_residencia"),
        FOTO("foto"),
        CERTIDAO_NASCIMENTO("certidao_nascimento"),
        DECLARACAO_ASSINADA("declaracao_assinada"),
        RENOVACAO_ASSINADA("renovacao_assinada"),
        FICHA_NATACAO_ASSINADA("ficha_natacao_assinada"),
        DESISTENCIA_ASSINADA("desistencia_assinada"),
        OUTRO("outro");
        
        private final String valor;
        
        TipoDocumento(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
    }
    
    @PrePersist
    protected void onCreate() {
        if (dataUpload == null) {
            dataUpload = LocalDateTime.now();
        }
    }
}
