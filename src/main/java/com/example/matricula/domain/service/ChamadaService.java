package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.*;
import com.example.matricula.domain.entity.AlunoOficina;
import com.example.matricula.domain.entity.Oficina;
import com.example.matricula.domain.entity.Presenca;
import com.example.matricula.domain.entity.Aula;
import com.example.matricula.domain.repository.AlunoOficinaRepository;
import com.example.matricula.domain.repository.OficinaRepository;
import com.example.matricula.domain.repository.PresencaRepository;
import com.example.matricula.domain.repository.AulaRepository;
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
    private final AulaRepository aulaRepository;
    
    /**
     * Lista todos os alunos de uma oficina para fazer a chamada
     */
    @Transactional
    public List<AlunoParaChamadaDTO> listarAlunosParaChamada(
            String oficinaId, String horarioId, LocalDate dataAula) {
        // Verifica se a oficina existe
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
        
        // Busca todos os alunos confirmados na oficina
        List<AlunoOficina> alunos = alunoOficinaRepository.findByOficinaIdAndStatus(
            oficinaId, 
            AlunoOficina.StatusInscricao.CONFIRMADO
        );

        if (horarioId != null && !horarioId.isBlank()) {
            alunos = alunos.stream()
                .filter(aluno -> horarioId.equals(aluno.getHorarioId()))
                .collect(Collectors.toList());
        }
        
        // Busca presenças já registradas para esta data (se houver)
        Map<String, Presenca> presencasMap = presencaRepository
            .findByOficinaIdAndDataAula(oficinaId, dataAula)
            .stream()
            .collect(Collectors.toMap(Presenca::getAlunoOficinaId, p -> p));
        
        // Monta a lista de alunos com status de presença (se já registrado)
        return alunos.stream().map(aluno -> {
            AlunoParaChamadaDTO dto = new AlunoParaChamadaDTO();
            dto.setAlunoOficinaId(aluno.getId());
            dto.setNomeCompleto(nomeDoAluno(aluno));
            dto.setIdade(aluno.getParticipanteAvulso() != null ? aluno.getParticipanteAvulso().getIdade() : aluno.getIdade());
            dto.setTurno(aluno.getParticipanteAvulso() != null ? aluno.getParticipanteAvulso().getTurno() : aluno.getTurno());
            
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
        
        if (request.getDataAula() == null) {
            throw new IllegalArgumentException("A data da aula é obrigatória");
        }
        if (request.getHorarioId() == null || request.getHorarioId().isBlank()) {
            throw new IllegalArgumentException("O horário da chamada é obrigatório");
        }
        Aula aula = aulaRepository.findByOficinaIdAndHorarioIdAndDataAula(
            oficinaId, request.getHorarioId(), request.getDataAula())
            .orElseGet(() -> {
                Aula nova = new Aula();
                nova.setOficinaId(oficinaId);
                nova.setHorarioId(request.getHorarioId());
                nova.setDataAula(request.getDataAula());
                nova.setCriadoPor(request.getRegistradoPor());
                return aulaRepository.save(nova);
            });

        List<Presenca> presencasRegistradas = new ArrayList<>();
        
        for (PresencaDTO presencaDTO : request.getPresencas()) {
            // Verifica se o aluno existe e pertence à oficina
            AlunoOficina aluno = alunoOficinaRepository.findById(presencaDTO.getAlunoOficinaId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
            
            if (!aluno.getOficinaId().equals(oficinaId)) {
                throw new RuntimeException("Aluno não pertence a esta oficina");
            }

            if (request.getHorarioId() == null || request.getHorarioId().isBlank()) {
                throw new RuntimeException("Horário da chamada é obrigatório");
            }

            if (!request.getHorarioId().equals(aluno.getHorarioId())) {
                throw new RuntimeException("Aluno não pertence ao horário selecionado");
            }
            
            // Verifica se já existe registro de presença para este aluno nesta data
            Presenca presenca = presencaRepository
                .findByAlunoOficinaIdAndAulaId(presencaDTO.getAlunoOficinaId(), aula.getId())
                .orElse(new Presenca());
            
            // Atualiza ou cria o registro
            presenca.setAlunoOficinaId(presencaDTO.getAlunoOficinaId());
            presenca.setOficinaId(oficinaId);
            presenca.setHorarioId(request.getHorarioId());
            presenca.setAulaId(aula.getId());
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
    @Transactional
    public List<HistoricoPresencaDTO> buscarHistoricoOficina(String oficinaId, String horarioId) {
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));
        
        List<Presenca> presencas = horarioId == null || horarioId.isBlank()
            ? presencaRepository.findByOficinaIdOrderByDataAulaDesc(oficinaId)
            : presencaRepository.findByOficinaIdAndHorarioIdOrderByDataAulaDesc(oficinaId, horarioId);
        
        return presencas.stream().map(presenca -> {
            HistoricoPresencaDTO dto = new HistoricoPresencaDTO();
            dto.setId(presenca.getId());
            dto.setAlunoOficinaId(presenca.getAlunoOficinaId());
            dto.setOficinaId(presenca.getOficinaId());
            dto.setNomeOficina(oficina.getNome());
            dto.setHorarioId(presenca.getHorarioId());
            dto.setAulaId(presenca.getAulaId());
            dto.setDataAula(presenca.getDataAula());
            dto.setStatus(presenca.getStatus());
            dto.setObservacao(presenca.getObservacao());
            dto.setDataRegistro(presenca.getDataRegistro());
            dto.setRegistradoPor(presenca.getRegistradoPor());
            
            // Busca nome do aluno
            alunoOficinaRepository.findById(presenca.getAlunoOficinaId())
                .ifPresent(aluno -> dto.setNomeAluno(nomeDoAluno(aluno)));
            
            return dto;
        }).collect(Collectors.toList());
    }
    
    /**
     * Busca o histórico de presença de um aluno específico
     */
    @Transactional
    public List<HistoricoPresencaDTO> buscarHistoricoAluno(String alunoOficinaId) {
        AlunoOficina aluno = alunoOficinaRepository.findById(alunoOficinaId)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        List<Presenca> presencas = presencaRepository.findByAlunoOficinaIdOrderByDataAulaDesc(alunoOficinaId);
        
        return presencas.stream().map(presenca -> {
            HistoricoPresencaDTO dto = new HistoricoPresencaDTO();
            dto.setId(presenca.getId());
            dto.setAlunoOficinaId(presenca.getAlunoOficinaId());
            dto.setNomeAluno(nomeDoAluno(aluno));
            dto.setOficinaId(presenca.getOficinaId());
            dto.setHorarioId(presenca.getHorarioId());
            dto.setAulaId(presenca.getAulaId());
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

    @Transactional
    public List<RelatorioFrequenciaDTO> gerarRelatorioFrequencia(
            String oficinaId, String horarioId, LocalDate inicio, LocalDate fim) {
        if (horarioId == null || horarioId.isBlank()) {
            throw new IllegalArgumentException("O horário é obrigatório para gerar o relatório");
        }
        List<Presenca> registros = presencaRepository
            .findByOficinaIdAndHorarioIdAndDataAulaBetweenOrderByDataAulaDesc(
                oficinaId, horarioId, inicio, fim);

        return registros.stream().collect(Collectors.groupingBy(Presenca::getAlunoOficinaId))
            .entrySet().stream().map(entry -> {
                List<Presenca> itens = entry.getValue();
                long presentes = itens.stream().filter(p -> p.getStatus() == Presenca.StatusPresenca.PRESENTE).count();
                long faltas = itens.stream().filter(p -> p.getStatus() == Presenca.StatusPresenca.FALTA).count();
                long justificadas = itens.stream().filter(p -> p.getStatus() == Presenca.StatusPresenca.JUSTIFICADA).count();
                String nome = alunoOficinaRepository.findById(entry.getKey())
                    .map(this::nomeDoAluno).orElse("Participante removido");
                double percentual = itens.isEmpty() ? 0 : (presentes * 100.0 / itens.size());
                return new RelatorioFrequenciaDTO(entry.getKey(), nome, itens.size(),
                    presentes, faltas, justificadas, Math.round(percentual * 10.0) / 10.0);
            }).sorted(java.util.Comparator.comparing(RelatorioFrequenciaDTO::getNomeAluno))
            .collect(Collectors.toList());
    }

    private String nomeDoAluno(AlunoOficina aluno) {
        return aluno.getParticipanteAvulso() != null
            ? aluno.getParticipanteAvulso().getNomeCompleto()
            : aluno.getNomeCompleto();
    }
}
