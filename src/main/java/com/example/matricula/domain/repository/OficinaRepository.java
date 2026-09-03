package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, String>, JpaSpecificationExecutor<Oficina> {
    
    List<Oficina> findByCategoriaIgnoreCase(String categoria);
    
    List<Oficina> findByStatus(Oficina.StatusOficina status);
    long countByStatus(Oficina.StatusOficina status);
    
    List<Oficina> findByResponsavelId(String responsavelId);
    
    List<Oficina> findByNomeContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String nome, String descricao);
}
