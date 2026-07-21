package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.AgentesCidadania;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgentesCidadaniaRepository extends JpaRepository<AgentesCidadania, String> {
    
    List<AgentesCidadania> findByStatus(AgentesCidadania.StatusAgente status);
    
    List<AgentesCidadania> findByNomeContainingIgnoreCase(String nome);
}
