package com.example.matricula.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "arquivos_upload")
@Data
@NoArgsConstructor
public class ArquivoUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nomeArquivo;

    @Column(nullable = false)
    private String tipoConteudo;

    @Column(nullable = false)
    private String categoria;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(nullable = false, columnDefinition = "bytea")
    private byte[] conteudo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataUpload;

    @PrePersist
    void onCreate() {
        dataUpload = LocalDateTime.now();
    }
}
