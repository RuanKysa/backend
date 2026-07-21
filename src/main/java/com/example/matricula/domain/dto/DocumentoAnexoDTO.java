package com.example.matricula.domain.dto;

import com.example.matricula.domain.entity.DocumentoAnexo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoAnexoDTO {
    
    private String id;
    private String tipo;
    private String nomeArquivo;
    private String url;
    private LocalDateTime dataUpload;
}
