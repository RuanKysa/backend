package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Presenca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PresencaRepository extends JpaRepository<Presenca, String> {
    
    // Buscar presença específica de um aluno em uma data
    Optional<Presenca> findByAlunoOficinaIdAndDataAula(String alunoOficinaId, LocalDate dataAula);
    Optional<Presenca> findByAlunoOficinaIdAndAulaId(String alunoOficinaId, String aulaId);
    
    // Listar todas as presenças de uma oficina em uma data específica
    List<Presenca> findByOficinaIdAndDataAula(String oficinaId, LocalDate dataAula);
    
    // Listar todas as presenças de uma oficina (histórico)
    List<Presenca> findByOficinaIdOrderByDataAulaDesc(String oficinaId);
    List<Presenca> findByOficinaIdAndHorarioIdOrderByDataAulaDesc(String oficinaId, String horarioId);
    List<Presenca> findByOficinaIdAndHorarioIdAndDataAulaBetweenOrderByDataAulaDesc(
        String oficinaId, String horarioId, LocalDate inicio, LocalDate fim);
    
    // Listar histórico de presença de um aluno específico
    List<Presenca> findByAlunoOficinaIdOrderByDataAulaDesc(String alunoOficinaId);
    
    // Contar presenças de um aluno
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.alunoOficinaId = :alunoOficinaId AND p.status = 'PRESENTE'")
    long contarPresencasPorAluno(@Param("alunoOficinaId") String alunoOficinaId);
    
    // Contar faltas de um aluno
    @Query("SELECT COUNT(p) FROM Presenca p WHERE p.alunoOficinaId = :alunoOficinaId AND p.status = 'FALTA'")
    long contarFaltasPorAluno(@Param("alunoOficinaId") String alunoOficinaId);
    
    // Verificar se já existe chamada registrada para uma oficina em uma data
    boolean existsByOficinaIdAndDataAula(String oficinaId, LocalDate dataAula);
    long countByDataAula(LocalDate dataAula);
    long countByDataAulaAndStatus(LocalDate dataAula, Presenca.StatusPresenca status);
}
