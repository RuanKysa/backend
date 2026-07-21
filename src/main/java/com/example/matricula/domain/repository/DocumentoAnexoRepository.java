package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.DocumentoAnexo;
import com.example.matricula.domain.entity.DocumentoAnexo.TipoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentoAnexoRepository extends JpaRepository<DocumentoAnexo, String> {
    
    // Busca documentos por tipo
    List<DocumentoAnexo> findByTipo(TipoDocumento tipo);
    
    // Busca documentos por matrícula
    List<DocumentoAnexo> findByMatriculaId(String matriculaId);
    
    // Busca documentos por matrícula e tipo
    List<DocumentoAnexo> findByMatriculaIdAndTipo(String matriculaId, TipoDocumento tipo);
}
