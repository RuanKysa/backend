package com.example.matricula.application.controller;

import com.example.matricula.domain.dto.AlunoParaChamadaDTO;
import com.example.matricula.domain.dto.ChamadaRequestDTO;
import com.example.matricula.domain.dto.HistoricoPresencaDTO;
import com.example.matricula.domain.entity.Presenca;
import com.example.matricula.domain.service.ChamadaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chamadas")
@RequiredArgsConstructor
@Tag(name = "Chamada", description = "Endpoints para gestão de chamada/presença nas oficinas")
public class ChamadaController {
    
    private final ChamadaService chamadaService;
    
    @GetMapping("/oficinas/{oficinaId}/alunos")
    @Operation(summary = "Listar alunos para chamada", 
               description = "Retorna lista de alunos de uma oficina para fazer a chamada. " +
                           "Se informar uma data, mostra também o status de presença já registrado (se houver).")
    public ResponseEntity<List<AlunoParaChamadaDTO>> listarAlunosParaChamada(
            @PathVariable String oficinaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        
        LocalDate dataAula = data != null ? data : LocalDate.now();
        List<AlunoParaChamadaDTO> alunos = chamadaService.listarAlunosParaChamada(oficinaId, dataAula);
        return ResponseEntity.ok(alunos);
    }
    
    @PostMapping("/oficinas/{oficinaId}/registrar")
    @Operation(summary = "Registrar chamada", 
               description = "Registra a presença/falta dos alunos de uma oficina em uma data específica. " +
                           "Se já houver chamada registrada para a data, será atualizada.")
    public ResponseEntity<List<Presenca>> registrarChamada(
            @PathVariable String oficinaId,
            @RequestBody ChamadaRequestDTO request) {
        
        List<Presenca> presencas = chamadaService.registrarChamada(oficinaId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(presencas);
    }
    
    @GetMapping("/oficinas/{oficinaId}/historico")
    @Operation(summary = "Histórico de chamadas da oficina", 
               description = "Retorna o histórico completo de presença de todos os alunos de uma oficina")
    public ResponseEntity<List<HistoricoPresencaDTO>> buscarHistoricoOficina(
            @PathVariable String oficinaId) {
        
        List<HistoricoPresencaDTO> historico = chamadaService.buscarHistoricoOficina(oficinaId);
        return ResponseEntity.ok(historico);
    }
    
    @GetMapping("/alunos/{alunoOficinaId}/historico")
    @Operation(summary = "Histórico de presença do aluno", 
               description = "Retorna o histórico de presença de um aluno específico")
    public ResponseEntity<List<HistoricoPresencaDTO>> buscarHistoricoAluno(
            @PathVariable String alunoOficinaId) {
        
        List<HistoricoPresencaDTO> historico = chamadaService.buscarHistoricoAluno(alunoOficinaId);
        return ResponseEntity.ok(historico);
    }
    
    @GetMapping("/alunos/{alunoOficinaId}/estatisticas")
    @Operation(summary = "Estatísticas de presença do aluno", 
               description = "Retorna estatísticas de presença/falta de um aluno")
    public ResponseEntity<Map<String, Long>> buscarEstatisticasAluno(
            @PathVariable String alunoOficinaId) {
        
        Map<String, Long> estatisticas = chamadaService.buscarEstatisticasAluno(alunoOficinaId);
        return ResponseEntity.ok(estatisticas);
    }
}
