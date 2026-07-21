package com.example.matricula.domain.repository;

import com.example.matricula.domain.entity.DistribuicaoAluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistribuicaoAlunoRepository extends JpaRepository<DistribuicaoAluno, String> {
    
    List<DistribuicaoAluno> findByAlunoId(String alunoId);
    
    List<DistribuicaoAluno> findByOficinaId(String oficinaId);
    
    List<DistribuicaoAluno> findByHorarioId(String horarioId);
    
    List<DistribuicaoAluno> findByStatus(DistribuicaoAluno.StatusDistribuicao status);
}
