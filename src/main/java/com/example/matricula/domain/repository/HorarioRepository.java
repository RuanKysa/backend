package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, String> {
    
    List<Horario> findByDiaSemana(Horario.DiaSemana diaSemana);
}
