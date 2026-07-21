package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.*;
import com.example.matricula.domain.entity.AlunoOficina;
import com.example.matricula.domain.entity.Oficina;
import com.example.matricula.domain.entity.Presenca;
import com.example.matricula.domain.repository.AlunoOficinaRepository;
import com.example.matricula.domain.repository.OficinaRepository;
import com.example.matricula.domain.repository.PresencaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChamadaService {
    
    private final PresencaRepository presencaRepository;
    private final AlunoOficinaRepository alunoOficinaRepository;
    private final OficinaRepository oficinaRepository;
    
    /**
     * Lista todos os alunos de uma oficina para fazer a chamada
     */
    public List<AlunoParaChamadaDTO> listarAlunosParaChamada(String oficinaId, LocalDate dataAula) {
        // Verifica se a oficina existe
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
        
        // Busca todos os alunos confirmados na oficina
        List<AlunoOficina> alunos = alunoOficinaRepository.findByOficinaIdAndStatus(
            oficinaId, 
            AlunoOficina.StatusInscricao.CONFIRMADO
        );
        
        // Busca presenças já registradas para esta data (se houver)
        Map<String, Presenca> presencasMap = presencaRepository
            .findByOficinaIdAndDataAula(oficinaId, dataAula)
            .stream()
            .collect(Collectors.toMap(Presenca::getAlunoOficinaId, p -> p));
        
        // Monta a lista de alunos com status de presença (se já registrado)
        return alunos.stream().map(aluno -> {
            AlunoParaChamadaDTO dto = new AlunoParaChamadaDTO();
            dto.setAlunoOficinaId(aluno.getId());
            dto.setNomeCompleto(aluno.getNomeCompleto());
            dto.setIdade(aluno.getIdade());
            dto.setTurno(aluno.getTurno());
            
            // Se já houver presença registrada, inclui o status
            if (presencasMap.containsKey(aluno.getId())) {
                dto.setStatusPresenca(presencasMap.get(aluno.getId()).getStatus());
            }
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Registra ou atualiza a chamada de uma oficina
     */
    @Transactional
    public List<Presenca> registrarChamada(String oficinaId, ChamadaRequestDTO request) {
        // Verifica se a oficina existe
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
        
        List<Presenca> presencasRegistradas = new ArrayList<>();
        
        for (PresencaDTO presencaDTO : request.getPresencas()) {
            // Verifica se o aluno existe e pertence à oficina
            AlunoOficina aluno = alunoOficinaRepository.findById(presencaDTO.getAlunoOficinaId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            
            if (!aluno.getOficinaId().equals(oficinaId)) {
                throw new RuntimeException("Aluno não pertence a esta oficina");
            }
            
            // Verifica se já existe registro de presença para este aluno nesta data
            Presenca presenca = presencaRepository
                .findByAlunoOficinaIdAndDataAula(presencaDTO.getAlunoOficinaId(), request.getDataAula())
                .orElse(new Presenca());
            
            // Atualiza ou cria o registro
            presenca.setAlunoOficinaId(presencaDTO.getAlunoOficinaId());
            presenca.setOficinaId(oficinaId);
            presenca.setDataAula(request.getDataAula());
            presenca.setStatus(presencaDTO.getStatus());
            presenca.setObservacao(presencaDTO.getObservacao());
            presenca.setRegistradoPor(request.getRegistradoPor());
            
            presencasRegistradas.add(presencaRepository.save(presenca));
        }
        
        return presencasRegistradas;
    }
    
    /**
     * Busca o histórico de presença de uma oficina
     */
    public List<HistoricoPresencaDTO> buscarHistoricoOficina(String oficinaId) {
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
        
        List<Presenca> presencas = presencaRepository.findByOficinaIdOrderByDataAulaDesc(oficinaId);
        
        return presencas.stream().map(presenca -> {
            HistoricoPresencaDTO dto = new HistoricoPresencaDTO();
            dto.setId(presenca.getId());
            dto.setAlunoOficinaId(presenca.getAlunoOficinaId());
            dto.setOficinaId(presenca.getOficinaId());
            dto.setNomeOficina(oficina.getNome());
            dto.setDataAula(presenca.getDataAula());
            dto.setStatus(presenca.getStatus());
            dto.setObservacao(presenca.getObservacao());
            dto.setDataRegistro(presenca.getDataRegistro());
            dto.setRegistradoPor(presenca.getRegistradoPor());
            
            // Busca nome do aluno
            alunoOficinaRepository.findById(presenca.getAlunoOficinaId())
                .ifPresent(aluno -> dto.setNomeAluno(aluno.getNomeCompleto()));
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Busca o histórico de presença de um aluno específico
     */
    public List<HistoricoPresencaDTO> buscarHistoricoAluno(String alunoOficinaId) {
        AlunoOficina aluno = alunoOficinaRepository.findById(alunoOficinaId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        List<Presenca> presencas = presencaRepository.findByAlunoOficinaIdOrderByDataAulaDesc(alunoOficinaId);
        
        return presencas.stream().map(presenca -> {
            HistoricoPresencaDTO dto = new HistoricoPresencaDTO();
            dto.setId(presenca.getId());
            dto.setAlunoOficinaId(presenca.getAlunoOficinaId());
            dto.setNomeAluno(aluno.getNomeCompleto());
            dto.setOficinaId(presenca.getOficinaId());
            dto.setDataAula(presenca.getDataAula());
            dto.setStatus(presenca.getStatus());
            dto.setObservacao(presenca.getObservacao());
            dto.setDataRegistro(presenca.getDataRegistro());
            dto.setRegistradoPor(presenca.getRegistradoPor());
            
            // Busca nome da oficina
            oficinaRepository.findById(presenca.getOficinaId())
                .ifPresent(oficina -> dto.setNomeOficina(oficina.getNome()));
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Busca estatísticas de presença de um aluno
     */
    public Map<String, Long> buscarEstatisticasAluno(String alunoOficinaId) {
        long presencas = presencaRepository.contarPresencasPorAluno(alunoOficinaId);
        long faltas = presencaRepository.contarFaltasPorAluno(alunoOficinaId);
        
        return Map.of(
            "presencas", presencas,
            "faltas", faltas,
            "total", presencas + faltas
        );
    }
}
