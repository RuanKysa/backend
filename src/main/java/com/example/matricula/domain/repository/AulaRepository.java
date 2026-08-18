package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AulaRepository extends JpaRepository<Aula, String> {
    Optional<Aula> findByOficinaIdAndHorarioIdAndDataAula(String oficinaId, String horarioId, LocalDate dataAula);
    List<Aula> findByOficinaIdAndHorarioIdOrderByDataAulaDesc(String oficinaId, String horarioId);
    long countByDataAula(LocalDate dataAula);
}
