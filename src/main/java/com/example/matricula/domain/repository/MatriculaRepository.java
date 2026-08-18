package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Matricula;
import com.example.matricula.domain.entity.Matricula.StatusMatricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, String> {
    
    // Busca por nome (case insensitive)
    List<Matricula> findByNomeCompletoContainingIgnoreCase(String nomeCompleto);
    
    // Busca por CPF
    List<Matricula> findByCpf(String cpf);
    
    // Busca por status
    List<Matricula> findByStatus(StatusMatricula status);
    long countByStatus(StatusMatricula status);
    
    // Busca por período
    List<Matricula> findByDataCadastroBetween(LocalDateTime dataInicio, LocalDateTime dataFim);
    
    // Busca com filtros combinados
    @Query("SELECT m FROM Matricula m WHERE " +
           "(:busca IS NULL OR LOWER(m.nomeCompleto) LIKE LOWER(CONCAT('%', :busca, '%')) " +
           "OR LOWER(m.cpf) LIKE LOWER(CONCAT('%', :busca, '%')) " +
           "OR LOWER(m.rg) LIKE LOWER(CONCAT('%', :busca, '%'))) " +
           "AND (:status IS NULL OR m.status = :status) " +
           "AND (:dataInicio IS NULL OR m.dataCadastro >= :dataInicio) " +
           "AND (:dataFim IS NULL OR m.dataCadastro <= :dataFim)")
    List<Matricula> findComFiltros(
        @Param("busca") String busca,
        @Param("status") StatusMatricula status,
        @Param("dataInicio") LocalDateTime dataInicio,
        @Param("dataFim") LocalDateTime dataFim
    );
    
    // Busca matrículas por turno SCFV
    List<Matricula> findByTurnoSCFV(Matricula.TurnoSCFV turno);
    
    // Busca matrículas assinadas
    List<Matricula> findByMatriculaAssinada(Boolean assinada);
}
