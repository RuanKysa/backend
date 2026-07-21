package com.example.matricula.application.controller;

import com.example.matricula.domain.dto.FiltrosMatriculaDTO;
import com.example.matricula.domain.dto.MatriculaRequestDTO;
import com.example.matricula.domain.dto.MatriculaResponseDTO;
import com.example.matricula.domain.service.MatriculaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/matriculas")
@RequiredArgsConstructor
@Tag(name = "Matrículas", description = "API de gerenciamento de matrículas")
public class MatriculaController {
    
    private final MatriculaService matriculaService;
    
    /**
     * POST /api/matriculas - Criar nova matrícula
     */
    @Operation(summary = "Criar matrícula", description = "Cria uma nova matrícula no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Matrícula criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MatriculaResponseDTO> criarMatricula(
            @Valid @RequestBody MatriculaRequestDTO requestDTO) {
        
        MatriculaResponseDTO response = matriculaService.criarMatricula(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * GET /api/matriculas - Listar todas as matrículas ou buscar com filtros
     */
    @Operation(summary = "Listar matrículas", description = "Lista todas as matrículas ou filtra por critérios")
    @ApiResponse(responseCode = "200", description = "Lista de matrículas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<MatriculaResponseDTO>> listarMatriculas(
            @Parameter(description = "Data inicial (yyyy-MM-dd)") @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data final (yyyy-MM-dd)") @RequestParam(required = false) String dataFim,
            @Parameter(description = "Busca por nome, CPF ou RG") @RequestParam(required = false) String busca,
            @Parameter(description = "Status: pendente, aprovada, rejeitada, aguardando_documentos") @RequestParam(required = false) String status) {
        
        // Se houver algum filtro, usar busca com filtros
        if (dataInicio != null || dataFim != null || busca != null || status != null) {
            FiltrosMatriculaDTO filtros = new FiltrosMatriculaDTO();
            filtros.setDataInicio(dataInicio);
            filtros.setDataFim(dataFim);
            filtros.setBusca(busca);
            filtros.setStatus(status);
            
            List<MatriculaResponseDTO> matriculas = matriculaService.buscarComFiltros(filtros);
            return ResponseEntity.ok(matriculas);
        }
        
        // Caso contrário, listar todas
        List<MatriculaResponseDTO> matriculas = matriculaService.listarTodas();
        return ResponseEntity.ok(matriculas);
    }
    
    /**
     * GET /api/matriculas/{id} - Buscar matrícula por ID
     */
    @Operation(summary = "Buscar por ID", description = "Busca uma matrícula específica pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula encontrada"),
        @ApiResponse(responseCode = "404", description = "Matrícula não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> buscarPorId(
            @Parameter(description = "ID da matrícula") @PathVariable String id) {
        try {
            MatriculaResponseDTO matricula = matriculaService.buscarPorId(id);
            return ResponseEntity.ok(matricula);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * PUT /api/matriculas/{id} - Atualizar matrícula existente
     */
    @Operation(summary = "Atualizar matrícula", description = "Atualiza todos os dados de uma matrícula existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Matrícula não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<MatriculaResponseDTO> atualizarMatricula(
            @Parameter(description = "ID da matrícula") @PathVariable String id,
            @Valid @RequestBody MatriculaRequestDTO requestDTO) {
        
        try {
            MatriculaResponseDTO response = matriculaService.atualizarMatricula(id, requestDTO);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/matriculas/{id} - Deletar matrícula
     */
    @Operation(summary = "Deletar matrícula", description = "Remove uma matrícula do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Matrícula deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Matrícula não encontrada", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMatricula(
            @Parameter(description = "ID da matrícula") @PathVariable String id) {
        try {
            matriculaService.deletarMatricula(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/matriculas/buscar/nome - Buscar matrículas por nome
     */
    @Operation(summary = "Buscar por nome", description = "Busca matrículas por nome completo (parcial)")
    @ApiResponse(responseCode = "200", description = "Lista de matrículas encontradas")
    @GetMapping("/buscar/nome")
    public ResponseEntity<List<MatriculaResponseDTO>> buscarPorNome(
            @Parameter(description = "Nome ou parte do nome") @RequestParam String nome) {
        
        List<MatriculaResponseDTO> matriculas = matriculaService.buscarPorNome(nome);
        return ResponseEntity.ok(matriculas);
    }
    
    /**
     * GET /api/matriculas/buscar/cpf - Buscar matrículas por CPF
     */
    @Operation(summary = "Buscar por CPF", description = "Busca matrículas pelo número do CPF")
    @ApiResponse(responseCode = "200", description = "Lista de matrículas encontradas")
    @GetMapping("/buscar/cpf")
    public ResponseEntity<List<MatriculaResponseDTO>> buscarPorCpf(
            @Parameter(description = "Número do CPF") @RequestParam String cpf) {
        
        List<MatriculaResponseDTO> matriculas = matriculaService.buscarPorCpf(cpf);
        return ResponseEntity.ok(matriculas);
    }
    
    /**
     * GET /api/matriculas/buscar/status - Buscar matrículas por status
     */
    @Operation(summary = "Buscar por status", description = "Busca matrículas filtradas por status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de matrículas encontradas"),
        @ApiResponse(responseCode = "400", description = "Status inválido", content = @Content)
    })
    @GetMapping("/buscar/status")
    public ResponseEntity<List<MatriculaResponseDTO>> buscarPorStatus(
            @Parameter(description = "Status: pendente, aprovada, rejeitada, aguardando_documentos") @RequestParam String status) {
        
        try {
            List<MatriculaResponseDTO> matriculas = matriculaService.buscarPorStatus(status);
            return ResponseEntity.ok(matriculas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PATCH /api/matriculas/{id}/status - Atualizar apenas o status da matrícula
     */
    @Operation(summary = "Atualizar status", description = "Atualiza apenas o status de uma matrícula")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Matrícula não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Status inválido", content = @Content)
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<MatriculaResponseDTO> atualizarStatus(
            @Parameter(description = "ID da matrícula") @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Novo status (JSON: {\"status\": \"aprovada\"})",
                required = true
            )
            @RequestBody Map<String, String> body) {
        
        try {
            String novoStatus = body.get("status");
            if (novoStatus == null || novoStatus.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MatriculaResponseDTO response = matriculaService.atualizarStatus(id, novoStatus);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PATCH /api/matriculas/{id}/assinatura - Assinar matrícula
     */
    @Operation(summary = "Assinar matrícula", description = "Adiciona assinatura digital à matrícula")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Matrícula assinada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Matrícula não encontrada", content = @Content),
        @ApiResponse(responseCode = "400", description = "Assinatura inválida", content = @Content)
    })
    @PatchMapping("/{id}/assinatura")
    public ResponseEntity<MatriculaResponseDTO> assinarMatricula(
            @Parameter(description = "ID da matrícula") @PathVariable String id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Assinatura em base64 (JSON: {\"assinatura\": \"data:image/png;base64,...\"})",
                required = true
            )
            @RequestBody Map<String, String> body) {
        
        try {
            String assinatura = body.get("assinatura");
            if (assinatura == null || assinatura.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            MatriculaResponseDTO response = matriculaService.assinarMatricula(id, assinatura);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/matriculas/stats - Obter estatísticas das matrículas
     */
    @Operation(summary = "Estatísticas", description = "Retorna estatísticas gerais das matrículas")
    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> obterEstatisticas() {
        List<MatriculaResponseDTO> todasMatriculas = matriculaService.listarTodas();
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", todasMatriculas.size());
        
        // Contar por status
        Map<String, Long> porStatus = new HashMap<>();
        todasMatriculas.forEach(m -> {
            String status = m.getStatus();
            porStatus.put(status, porStatus.getOrDefault(status, 0L) + 1);
        });
        stats.put("porStatus", porStatus);
        
        return ResponseEntity.ok(stats);
    }
    
    /**
     * Exception handler para validações
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception e) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
