package com.example.matricula.application.controller;

import com.example.matricula.domain.dto.*;
import com.example.matricula.domain.service.OficinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oficinas")
@RequiredArgsConstructor
@Tag(name = "Oficinas", description = "API de gerenciamento de oficinas")
public class OficinaController {
    
    private final OficinaService oficinaService;
    
    // ========== ENDPOINTS DE OFICINAS ==========
    
    @Operation(summary = "Criar oficina", description = "Cria uma nova oficina no sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Oficina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<OficinaResponseDTO> criarOficina(
            @Valid @RequestBody OficinaRequestDTO requestDTO) {
        
        OficinaResponseDTO response = oficinaService.criarOficina(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Listar oficinas", description = "Lista todas as oficinas ou filtra por critérios")
    @ApiResponse(responseCode = "200", description = "Lista de oficinas retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<OficinaResponseDTO>> listarOficinas(
            @Parameter(description = "Categoria da oficina") @RequestParam(required = false) String categoria,
            @Parameter(description = "Status da oficina") @RequestParam(required = false) String status,
            @Parameter(description = "ID do responsável") @RequestParam(required = false) String responsavel,
            @Parameter(description = "Dia da semana") @RequestParam(required = false) String diaSemana,
            @Parameter(description = "Data inicial (yyyy-MM-dd)") @RequestParam(required = false) String dataInicio,
            @Parameter(description = "Data final (yyyy-MM-dd)") @RequestParam(required = false) String dataFim,
            @Parameter(description = "Pesquisa por nome ou descrição") @RequestParam(required = false) String pesquisa) {
        
        // Se houver algum filtro, usar busca com filtros
        if (categoria != null || status != null || responsavel != null || diaSemana != null ||
            dataInicio != null || dataFim != null || pesquisa != null) {
            
            FiltrosOficinaDTO filtros = new FiltrosOficinaDTO();
            filtros.setCategoria(categoria);
            filtros.setStatus(status);
            filtros.setResponsavel(responsavel);
            filtros.setDiaSemana(diaSemana);
            filtros.setDataInicio(dataInicio);
            filtros.setDataFim(dataFim);
            filtros.setPesquisa(pesquisa);
            
            List<OficinaResponseDTO> oficinas = oficinaService.buscarComFiltros(filtros);
            return ResponseEntity.ok(oficinas);
        }
        
        // Caso contrário, listar todas
        List<OficinaResponseDTO> oficinas = oficinaService.listarTodas();
        return ResponseEntity.ok(oficinas);
    }
    
    @Operation(summary = "Buscar oficina por ID", description = "Retorna os detalhes de uma oficina específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oficina encontrada"),
        @ApiResponse(responseCode = "404", description = "Oficina não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OficinaResponseDTO> buscarPorId(@PathVariable String id) {
        OficinaResponseDTO oficina = oficinaService.buscarPorId(id);
        return ResponseEntity.ok(oficina);
    }
    
    @Operation(summary = "Atualizar oficina", description = "Atualiza os dados de uma oficina existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oficina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Oficina não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OficinaResponseDTO> atualizarOficina(
            @PathVariable String id,
            @Valid @RequestBody OficinaRequestDTO requestDTO) {
        
        OficinaResponseDTO response = oficinaService.atualizarOficina(id, requestDTO);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Deletar oficina", description = "Remove uma oficina do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Oficina deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Oficina não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOficina(@PathVariable String id) {
        oficinaService.deletarOficina(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Obter resumo", description = "Retorna um resumo estatístico das oficinas")
    @ApiResponse(responseCode = "200", description = "Resumo retornado com sucesso")
    @GetMapping("/resumo")
    public ResponseEntity<ResumoOficinaDTO> obterResumo() {
        ResumoOficinaDTO resumo = oficinaService.obterResumo();
        return ResponseEntity.ok(resumo);
    }
    
    // ========== ENDPOINTS DE RESPONSAVEIS ==========
    
    @Operation(summary = "Criar responsável", description = "Cria um novo responsável de oficina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Responsável criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/responsaveis")
    public ResponseEntity<ResponsavelDTO> criarResponsavel(
            @Valid @RequestBody ResponsavelDTO dto) {
        
        ResponsavelDTO response = oficinaService.criarResponsavel(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Listar responsáveis", description = "Lista todos os responsáveis de oficinas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/responsaveis")
    public ResponseEntity<List<ResponsavelDTO>> listarResponsaveis() {
        List<ResponsavelDTO> responsaveis = oficinaService.listarResponsaveis();
        return ResponseEntity.ok(responsaveis);
    }
    
    @Operation(summary = "Buscar responsável por ID", description = "Retorna os detalhes de um responsável")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Responsável encontrado"),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado")
    })
    @GetMapping("/responsaveis/{id}")
    public ResponseEntity<ResponsavelDTO> buscarResponsavelPorId(@PathVariable String id) {
        ResponsavelDTO responsavel = oficinaService.buscarResponsavelPorId(id);
        return ResponseEntity.ok(responsavel);
    }
    
    @Operation(summary = "Atualizar responsável", description = "Atualiza os dados de um responsável")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Responsável atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/responsaveis/{id}")
    public ResponseEntity<ResponsavelDTO> atualizarResponsavel(
            @PathVariable String id,
            @Valid @RequestBody ResponsavelDTO dto) {
        
        ResponsavelDTO response = oficinaService.atualizarResponsavel(id, dto);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Deletar responsável", description = "Remove um responsável do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Responsável deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Responsável não encontrado")
    })
    @DeleteMapping("/responsaveis/{id}")
    public ResponseEntity<Void> deletarResponsavel(@PathVariable String id) {
        oficinaService.deletarResponsavel(id);
        return ResponseEntity.noContent().build();
    }
    
    // ========== ENDPOINTS DE AGENTES DE CIDADANIA ==========
    
    @Operation(summary = "Criar agente de cidadania", description = "Cria um novo agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Agente criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping("/agentes-cidadania")
    public ResponseEntity<AgentesCidadaniaDTO> criarAgenteCidadania(
            @Valid @RequestBody AgentesCidadaniaDTO dto) {
        
        System.out.println("\n========== CRIAR AGENTE DE CIDADANIA ==========");
        System.out.println("Dados recebidos:");
        System.out.println("  Nome: " + dto.getNome());
        System.out.println("  Email: " + dto.getEmail());
        System.out.println("  CPF: " + dto.getCpf());
        System.out.println("  Data de Início: " + dto.getDataInicio());
        System.out.println("  Status: " + dto.getStatus());
        System.out.println("  Oficinas IDs: " + dto.getOficinasIds());
        System.out.println("===============================================\n");
        
        AgentesCidadaniaDTO response = oficinaService.criarAgenteCidadania(dto);
        
        System.out.println("\n========== AGENTE CRIADO COM SUCESSO ==========");
        System.out.println("  ID gerado: " + response.getId());
        System.out.println("===============================================\n");
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Listar agentes de cidadania", description = "Lista todos os agentes de cidadania")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/agentes-cidadania")
    public ResponseEntity<List<AgentesCidadaniaDTO>> listarAgentesCidadania() {
        List<AgentesCidadaniaDTO> agentes = oficinaService.listarAgentesCidadania();
        return ResponseEntity.ok(agentes);
    }
    
    @Operation(summary = "Buscar agente por ID", description = "Retorna os detalhes de um agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agente encontrado"),
        @ApiResponse(responseCode = "404", description = "Agente não encontrado")
    })
    @GetMapping("/agentes-cidadania/{id}")
    public ResponseEntity<AgentesCidadaniaDTO> buscarAgenteCidadaniaPorId(@PathVariable String id) {
        AgentesCidadaniaDTO agente = oficinaService.buscarAgenteCidadaniaPorId(id);
        return ResponseEntity.ok(agente);
    }
    
    @Operation(summary = "Atualizar agente", description = "Atualiza os dados de um agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Agente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/agentes-cidadania/{id}")
    public ResponseEntity<AgentesCidadaniaDTO> atualizarAgenteCidadania(
            @PathVariable String id,
            @RequestBody AgentesCidadaniaDTO dto) {
        
        System.out.println("\n========== ATUALIZAR AGENTE DE CIDADANIA ==========");
        System.out.println("  ID do Agente: " + id);
        System.out.println("Dados atualizados:");
        System.out.println("  Nome: " + dto.getNome());
        System.out.println("  Email: " + dto.getEmail());
        System.out.println("  Status: " + dto.getStatus());
        System.out.println("  Oficinas IDs: " + dto.getOficinasIds());
        System.out.println("===================================================\n");
        
        AgentesCidadaniaDTO response = oficinaService.atualizarAgenteCidadaniaParcial(id, dto);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Deletar agente", description = "Remove um agente de cidadania do sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Agente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agente não encontrado")
    })
    @DeleteMapping("/agentes-cidadania/{id}")
    public ResponseEntity<Void> deletarAgenteCidadania(@PathVariable String id) {
        oficinaService.deletarAgenteCidadania(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Adicionar oficina ao agente", description = "Vincula uma oficina a um agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Oficina vinculada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agente ou oficina não encontrados")
    })
    @PostMapping("/agentes-cidadania/{agenteId}/oficinas/{oficinaId}")
    public ResponseEntity<AgentesCidadaniaDTO> adicionarOficinaAoAgente(
            @PathVariable String agenteId,
            @PathVariable String oficinaId) {
        
        System.out.println("\n========== VINCULAR OFICINA AO AGENTE ==========");
        System.out.println("  Agente ID: " + agenteId);
        System.out.println("  Oficina ID: " + oficinaId);
        System.out.println("================================================\n");
        
        AgentesCidadaniaDTO response = oficinaService.adicionarOficinaAoAgente(agenteId, oficinaId);
        
        System.out.println("\n========== VÍNCULO CRIADO COM SUCESSO ==========");
        System.out.println("  Oficinas vinculadas: " + response.getOficinasIds());
        System.out.println("================================================\n");
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Remover oficina do agente", description = "Remove o vínculo de uma oficina com um agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vínculo removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agente ou oficina não encontrados")
    })
    @DeleteMapping("/agentes-cidadania/{agenteId}/oficinas/{oficinaId}")
    public ResponseEntity<AgentesCidadaniaDTO> removerOficinaDoAgente(
            @PathVariable String agenteId,
            @PathVariable String oficinaId) {
        
        System.out.println("\n========== REMOVER VÍNCULO OFICINA-AGENTE ==========");
        System.out.println("  Agente ID: " + agenteId);
        System.out.println("  Oficina ID: " + oficinaId);
        System.out.println("====================================================\n");
        
        AgentesCidadaniaDTO response = oficinaService.removerOficinaDoAgente(agenteId, oficinaId);
        
        System.out.println("\n========== VÍNCULO REMOVIDO COM SUCESSO ==========");
        System.out.println("  Oficinas restantes: " + response.getOficinasIds());
        System.out.println("==================================================\n");
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar oficinas do agente", description = "Retorna as oficinas vinculadas a um agente de cidadania")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Agente não encontrado")
    })
    @GetMapping({"/agentes-cidadania/{agenteId}/oficinas", "/agentes-cidadania/{agenteId}/oficinas."})
    public ResponseEntity<List<OficinaResponseDTO>> listarOficinasDoAgente(@PathVariable String agenteId) {
        List<OficinaResponseDTO> oficinas = oficinaService.listarOficinasDoAgente(agenteId);
        return ResponseEntity.ok(oficinas);
    }
    
    // ========== ENDPOINTS DE ALUNOS OFICINA ==========
    
    @Operation(summary = "Inscrever aluno em oficina", description = "Cria uma nova inscrição de aluno em oficina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Aluno inscrito com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou sem vagas disponíveis")
    })
    @PostMapping("/alunos")
    public ResponseEntity<AlunoOficinaDTO> inscreverAluno(
            @Valid @RequestBody AlunoOficinaDTO dto) {
        
        AlunoOficinaDTO response = oficinaService.inscreverAluno(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Listar alunos de oficinas", description = "Lista todos os alunos inscritos em oficinas")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/alunos")
    public ResponseEntity<List<AlunoOficinaDTO>> listarAlunosOficina() {
        List<AlunoOficinaDTO> alunos = oficinaService.listarAlunosOficina();
        return ResponseEntity.ok(alunos);
    }
    
    @Operation(summary = "Buscar inscrição por ID", description = "Retorna os detalhes de uma inscrição")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscrição encontrada"),
        @ApiResponse(responseCode = "404", description = "Inscrição não encontrada")
    })
    @GetMapping("/alunos/{id}")
    public ResponseEntity<AlunoOficinaDTO> buscarAlunoOficinaPorId(@PathVariable String id) {
        AlunoOficinaDTO aluno = oficinaService.buscarAlunoOficinaPorId(id);
        return ResponseEntity.ok(aluno);
    }
    
    @Operation(summary = "Listar oficinas por matrícula", description = "Lista todas as oficinas de um aluno")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/alunos/matricula/{matriculaId}")
    public ResponseEntity<List<AlunoOficinaDTO>> listarAlunosPorMatricula(
            @PathVariable String matriculaId) {
        
        List<AlunoOficinaDTO> alunos = oficinaService.listarAlunosPorMatricula(matriculaId);
        return ResponseEntity.ok(alunos);
    }
    
    @Operation(summary = "Listar alunos por oficina", description = "Lista todos os alunos de uma oficina")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping("/alunos/oficina/{oficinaId}")
    public ResponseEntity<List<AlunoOficinaDTO>> listarAlunosPorOficina(
            @PathVariable String oficinaId) {
        
        List<AlunoOficinaDTO> alunos = oficinaService.listarAlunosPorOficina(oficinaId);
        return ResponseEntity.ok(alunos);
    }
    
    @Operation(summary = "Atualizar inscrição", description = "Atualiza os dados de uma inscrição")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inscrição atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Inscrição não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/alunos/{id}")
    public ResponseEntity<AlunoOficinaDTO> atualizarInscricao(
            @PathVariable String id,
            @Valid @RequestBody AlunoOficinaDTO dto) {
        
        AlunoOficinaDTO response = oficinaService.atualizarInscricao(id, dto);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Cancelar inscrição", description = "Cancela a inscrição de um aluno em uma oficina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Inscrição cancelada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Inscrição não encontrada")
    })
    @DeleteMapping("/alunos/{id}")
    public ResponseEntity<Void> cancelarInscricao(@PathVariable String id) {
        oficinaService.cancelarInscricao(id);
        return ResponseEntity.noContent().build();
    }
}
