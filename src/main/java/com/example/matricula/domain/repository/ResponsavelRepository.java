package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponsavelRepository extends JpaRepository<Responsavel, String> {
    
    List<Responsavel> findByTipo(Responsavel.TipoResponsavel tipo);
    
    List<Responsavel> findByNomeContainingIgnoreCase(String nome);
}
