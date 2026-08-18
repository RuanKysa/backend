package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, String> {
    
    List<Horario> findByDiaSemana(Horario.DiaSemana diaSemana);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select h from Horario h where h.id = :id")
    Optional<Horario> findByIdForUpdate(@Param("id") String id);
}
