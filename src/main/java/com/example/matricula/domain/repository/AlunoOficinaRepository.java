package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.AlunoOficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlunoOficinaRepository extends JpaRepository<AlunoOficina, String> {
    
    List<AlunoOficina> findByMatriculaId(String matriculaId);
    
    List<AlunoOficina> findByOficinaId(String oficinaId);
    
    List<AlunoOficina> findByOficinaIdAndStatus(String oficinaId, AlunoOficina.StatusInscricao status);
    
    List<AlunoOficina> findByHorarioId(String horarioId);
    
    List<AlunoOficina> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
}
