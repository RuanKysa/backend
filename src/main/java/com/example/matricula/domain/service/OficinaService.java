package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.*;
import com.example.matricula.domain.entity.*;
import com.example.matricula.domain.mapper.OficinaMapper;
import com.example.matricula.domain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OficinaService {
    
    private final OficinaRepository oficinaRepository;
    private final ResponsavelRepository responsavelRepository;
    private final AgentesCidadaniaRepository agentesCidadaniaRepository;
    private final AlunoOficinaRepository alunoOficinaRepository;
    private final OficinaMapper oficinaMapper;
    
    // ========== OFICINA ==========
    
    @Transactional
    public OficinaResponseDTO criarOficina(OficinaRequestDTO requestDTO) {
        Oficina oficina = oficinaMapper.toEntity(requestDTO);
        
        // Associar responsável se informado
        if (requestDTO.getResponsavelId() != null) {
            Responsavel responsavel = responsavelRepository.findById(requestDTO.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));
            oficina.setResponsavel(responsavel);
        }
        
        // Salvar oficina
        Oficina oficinaSalva = oficinaRepository.save(oficina);
        
        return oficinaMapper.toResponseDTO(oficinaSalva);
    }
    
    @Transactional(readOnly = true)
    public List<OficinaResponseDTO> listarTodas() {
        return oficinaRepository.findAll().stream()
            .map(oficinaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public OficinaResponseDTO buscarPorId(String id) {
        Oficina oficina = oficinaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        return oficinaMapper.toResponseDTO(oficina);
    }
    
    @Transactional
    public OficinaResponseDTO atualizarOficina(String id, OficinaRequestDTO requestDTO) {
        Oficina oficina = oficinaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        // Atualizar dados da oficina
        oficinaMapper.updateEntityFromDTO(oficina, requestDTO);
        
        // Atualizar responsável se informado
        if (requestDTO.getResponsavelId() != null) {
            Responsavel responsavel = responsavelRepository.findById(requestDTO.getResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));
            oficina.setResponsavel(responsavel);
        } else {
            oficina.setResponsavel(null);
        }
        
        // Salvar alterações
        Oficina oficinaSalva = oficinaRepository.save(oficina);
        
        return oficinaMapper.toResponseDTO(oficinaSalva);
    }
    
    @Transactional
    public void deletarOficina(String id) {
        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        // Remover associações na tabela agente_oficina antes de deletar
        for (AgentesCidadania agente : new ArrayList<>(oficina.getAgentes())) {
            agente.getOficinas().remove(oficina);
        }
        oficina.getAgentes().clear();
        
        oficinaRepository.delete(oficina);
    }
    
    @Transactional(readOnly = true)
    public List<OficinaResponseDTO> buscarComFiltros(FiltrosOficinaDTO filtros) {
        Specification<Oficina> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Filtrar por categoria
            if (filtros.getCategoria() != null && !filtros.getCategoria().isEmpty()) {
                try {
                    Oficina.CategoriaOficina categoria = Oficina.CategoriaOficina.valueOf(filtros.getCategoria().toUpperCase());
                    predicates.add(cb.equal(root.get("categoria"), categoria));
                } catch (IllegalArgumentException ignored) {
                }
            }
            
            // Filtrar por status
            if (filtros.getStatus() != null && !filtros.getStatus().isEmpty()) {
                try {
                    Oficina.StatusOficina status = Oficina.StatusOficina.valueOf(filtros.getStatus().toUpperCase());
                    predicates.add(cb.equal(root.get("status"), status));
                } catch (IllegalArgumentException ignored) {
                }
            }
            
            // Filtrar por responsável
            if (filtros.getResponsavel() != null && !filtros.getResponsavel().isEmpty()) {
                predicates.add(cb.equal(root.get("responsavel").get("id"), filtros.getResponsavel()));
            }
            
            // Filtrar por data de início
            if (filtros.getDataInicio() != null && !filtros.getDataInicio().isEmpty()) {
                LocalDate dataInicio = LocalDate.parse(filtros.getDataInicio());
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataInicio"), dataInicio));
            }
            
            // Filtrar por data de fim
            if (filtros.getDataFim() != null && !filtros.getDataFim().isEmpty()) {
                LocalDate dataFim = LocalDate.parse(filtros.getDataFim());
                predicates.add(cb.lessThanOrEqualTo(root.get("dataFim"), dataFim));
            }
            
            // Pesquisa por nome ou descrição
            if (filtros.getPesquisa() != null && !filtros.getPesquisa().isEmpty()) {
                String pesquisa = "%" + filtros.getPesquisa().toLowerCase() + "%";
                Predicate nomeMatch = cb.like(cb.lower(root.get("nome")), pesquisa);
                Predicate descricaoMatch = cb.like(cb.lower(root.get("descricao")), pesquisa);
                predicates.add(cb.or(nomeMatch, descricaoMatch));
            }
            
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        return oficinaRepository.findAll(spec).stream()
            .map(oficinaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ResumoOficinaDTO obterResumo() {
        ResumoOficinaDTO resumo = new ResumoOficinaDTO();
        
        // Total de oficinas
        resumo.setTotalOficinas((int) oficinaRepository.count());
        
        // Oficinas ativas
        List<Oficina> oficinasAtivas = oficinaRepository.findByStatus(Oficina.StatusOficina.ATIVA);
        resumo.setOficinasAtivas(oficinasAtivas.size());
        
        // Total de vagas e vagas ocupadas
        List<Oficina> todasOficinas = oficinaRepository.findAll();
        int totalVagas = todasOficinas.stream()
            .mapToInt(Oficina::getVagasTotais)
            .sum();
        int vagasOcupadas = todasOficinas.stream()
            .mapToInt(o -> o.getVagasTotais() - o.getVagasDisponiveis())
            .sum();
        
        resumo.setTotalVagas(totalVagas);
        resumo.setVagasOcupadas(vagasOcupadas);
        
        // Alunos inscritos
        resumo.setAlunosInscritos((int) alunoOficinaRepository.count());
        
        // Responsáveis ativos (todos cadastrados são considerados ativos)
        resumo.setResponsaveisAtivos((int) responsavelRepository.count());
        
        // Agentes de cidadania ativos
        List<AgentesCidadania> agentesAtivos = agentesCidadaniaRepository.findByStatus(AgentesCidadania.StatusAgente.ATIVO);
        resumo.setAgentesCidadaniaAtivos(agentesAtivos.size());
        
        return resumo;
    }
    
    // ========== RESPONSAVEL ==========
    
    @Transactional
    public ResponsavelDTO criarResponsavel(ResponsavelDTO dto) {
        Responsavel responsavel = oficinaMapper.toResponsavelEntity(dto);
        Responsavel responsavelSalvo = responsavelRepository.save(responsavel);
        
        return oficinaMapper.toResponsavelDTO(responsavelSalvo);
    }
    
    @Transactional(readOnly = true)
    public List<ResponsavelDTO> listarResponsaveis() {
        return responsavelRepository.findAll().stream()
            .map(oficinaMapper::toResponsavelDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ResponsavelDTO buscarResponsavelPorId(String id) {
        Responsavel responsavel = responsavelRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));
        
        return oficinaMapper.toResponsavelDTO(responsavel);
    }
    
    @Transactional
    public ResponsavelDTO atualizarResponsavel(String id, ResponsavelDTO dto) {
        if (!responsavelRepository.existsById(id)) {
            throw new EntityNotFoundException("Responsável não encontrado");
        }
        
        dto.setId(id);
        Responsavel responsavel = oficinaMapper.toResponsavelEntity(dto);
        Responsavel responsavelSalvo = responsavelRepository.save(responsavel);
        
        return oficinaMapper.toResponsavelDTO(responsavelSalvo);
    }
    
    @Transactional
    public void deletarResponsavel(String id) {
        Responsavel responsavel = responsavelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Responsável não encontrado"));
        
        // Desvincula o responsável das oficinas antes de deletar
        List<Oficina> oficinas = oficinaRepository.findByResponsavelId(id);
        for (Oficina oficina : oficinas) {
            oficina.setResponsavel(null);
        }
        oficinaRepository.saveAll(oficinas);
        
        responsavelRepository.delete(responsavel);
    }
    
    // ========== AGENTES CIDADANIA ==========
    
    @Transactional
    public AgentesCidadaniaDTO criarAgenteCidadania(AgentesCidadaniaDTO dto) {
        AgentesCidadania agente = oficinaMapper.toAgentesCidadaniaEntity(dto);
        
        // Adicionar oficinas se fornecidas
        if (dto.getOficinasIds() != null && !dto.getOficinasIds().isEmpty()) {
            for (String oficinaId : dto.getOficinasIds()) {
                Oficina oficina = oficinaRepository.findById(oficinaId)
                    .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada: " + oficinaId));
                agente.addOficina(oficina);
            }
        }
        
        AgentesCidadania agenteSalvo = agentesCidadaniaRepository.save(agente);
        return oficinaMapper.toAgentesCidadaniaDTO(agenteSalvo);
    }
    
    @Transactional(readOnly = true)
    public List<AgentesCidadaniaDTO> listarAgentesCidadania() {
        return agentesCidadaniaRepository.findAll().stream()
            .map(oficinaMapper::toAgentesCidadaniaDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AgentesCidadaniaDTO buscarAgenteCidadaniaPorId(String id) {
        AgentesCidadania agente = agentesCidadaniaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));
        
        return oficinaMapper.toAgentesCidadaniaDTO(agente);
    }
    
    @Transactional
    public AgentesCidadaniaDTO atualizarAgenteCidadania(String id, AgentesCidadaniaDTO dto) {
        AgentesCidadania agente = agentesCidadaniaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));
        
        // Atualizar campos básicos
        agente.setNome(dto.getNome());
        agente.setDataNascimento(dto.getDataNascimento());
        agente.setCpf(dto.getCpf());
        agente.setEmail(dto.getEmail());
        agente.setTelefone(dto.getTelefone());
        agente.setEndereco(dto.getEndereco());
        agente.setDataInicio(dto.getDataInicio());
        
        if (dto.getStatus() != null) {
            agente.setStatus(AgentesCidadania.StatusAgente.valueOf(dto.getStatus().toUpperCase()));
        }
        
        // Sincronizar oficinas (relacionamento ManyToMany)
        if (dto.getOficinasIds() != null) {
            // Limpar oficinas existentes
            agente.getOficinas().clear();
            
            // Adicionar novas oficinas
            for (String oficinaId : dto.getOficinasIds()) {
                Oficina oficina = oficinaRepository.findById(oficinaId)
                    .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada: " + oficinaId));
                agente.addOficina(oficina);
            }
        }
        
        AgentesCidadania agenteSalvo = agentesCidadaniaRepository.save(agente);
        return oficinaMapper.toAgentesCidadaniaDTO(agenteSalvo);
    }
    
    @Transactional
    public void deletarAgenteCidadania(String id) {
        if (!agentesCidadaniaRepository.existsById(id)) {
            throw new EntityNotFoundException("Agente de cidadania não encontrado");
        }
        
        agentesCidadaniaRepository.deleteById(id);
    }
    
    /**
     * Atualizar parcialmente um Agente de Cidadania
     */
    @Transactional
    public AgentesCidadaniaDTO atualizarAgenteCidadaniaParcial(String id, AgentesCidadaniaDTO dto) {
        AgentesCidadania agente = agentesCidadaniaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));
        
        // Atualizar apenas os campos não nulos
        if (dto.getNome() != null) {
            agente.setNome(dto.getNome());
        }
        if (dto.getDataNascimento() != null) {
            agente.setDataNascimento(dto.getDataNascimento());
        }
        if (dto.getCpf() != null) {
            agente.setCpf(dto.getCpf());
        }
        if (dto.getEmail() != null) {
            agente.setEmail(dto.getEmail());
        }
        if (dto.getTelefone() != null) {
            agente.setTelefone(dto.getTelefone());
        }
        if (dto.getEndereco() != null) {
            agente.setEndereco(dto.getEndereco());
        }
        if (dto.getDataInicio() != null) {
            agente.setDataInicio(dto.getDataInicio());
        }
        if (dto.getStatus() != null) {
            agente.setStatus(AgentesCidadania.StatusAgente.valueOf(dto.getStatus().toUpperCase()));
        }
        
        // Sincronizar oficinas (relacionamento ManyToMany)
        if (dto.getOficinasIds() != null) {
            agente.getOficinas().clear();
            for (String oficinaId : dto.getOficinasIds()) {
                Oficina oficina = oficinaRepository.findById(oficinaId)
                    .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada: " + oficinaId));
                agente.addOficina(oficina);
            }
        }
        
        AgentesCidadania agenteSalvo = agentesCidadaniaRepository.save(agente);
        return oficinaMapper.toAgentesCidadaniaDTO(agenteSalvo);
    }
    
    /**
     * Adicionar uma oficina a um Agente de Cidadania
     */
    @Transactional
    public AgentesCidadaniaDTO adicionarOficinaAoAgente(String agenteId, String oficinaId) {
        System.out.println("\n[SERVICE] Iniciando vinculação...");
        
        AgentesCidadania agente = agentesCidadaniaRepository.findById(agenteId)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));
        
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        System.out.println("[SERVICE] Agente encontrado: " + agente.getNome());
        System.out.println("[SERVICE] Oficina encontrada: " + oficina.getNome());
        System.out.println("[SERVICE] Oficinas do agente ANTES: " + agente.getOficinas().size());
        
        // Adicionar oficina ao agente
        agente.addOficina(oficina);
        
        System.out.println("[SERVICE] Oficinas do agente DEPOIS de add: " + agente.getOficinas().size());
        
        AgentesCidadania agenteSalvo = agentesCidadaniaRepository.save(agente);
        
        System.out.println("[SERVICE] Agente salvo! Oficinas após save: " + agenteSalvo.getOficinas().size());
        System.out.println("[SERVICE] IDs das oficinas: " + agenteSalvo.getOficinas().stream()
            .map(Oficina::getId).toList());
        
        return oficinaMapper.toAgentesCidadaniaDTO(agenteSalvo);
    }
    
    /**
     * Remover uma oficina de um Agente de Cidadania
     */
    @Transactional
    public AgentesCidadaniaDTO removerOficinaDoAgente(String agenteId, String oficinaId) {
        AgentesCidadania agente = agentesCidadaniaRepository.findById(agenteId)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));
        
        Oficina oficina = oficinaRepository.findById(oficinaId)
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        // Remover oficina do agente
        agente.removeOficina(oficina);
        
        AgentesCidadania agenteSalvo = agentesCidadaniaRepository.save(agente);
        return oficinaMapper.toAgentesCidadaniaDTO(agenteSalvo);
    }

    @Transactional(readOnly = true)
    public List<OficinaResponseDTO> listarOficinasDoAgente(String agenteId) {
        AgentesCidadania agente = agentesCidadaniaRepository.findById(agenteId)
            .orElseThrow(() -> new EntityNotFoundException("Agente de cidadania não encontrado"));

        return agente.getOficinas().stream()
            .map(oficinaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    // ========== ALUNO OFICINA ==========
    
    @Transactional
    public AlunoOficinaDTO inscreverAluno(AlunoOficinaDTO dto) {
        // Verificar se a oficina existe
        Oficina oficina = oficinaRepository.findById(dto.getOficinaId())
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        // Verificar se há vagas disponíveis
        if (oficina.getVagasDisponiveis() <= 0) {
            throw new IllegalStateException("Não há vagas disponíveis nesta oficina");
        }
        
        // Criar inscrição
        AlunoOficina aluno = oficinaMapper.toAlunoOficinaEntity(dto);
        AlunoOficina alunoSalvo = alunoOficinaRepository.save(aluno);
        
        // Atualizar vagas disponíveis
        oficina.setVagasDisponiveis(oficina.getVagasDisponiveis() - 1);
        oficinaRepository.save(oficina);
        
        return oficinaMapper.toAlunoOficinaDTO(alunoSalvo);
    }
    
    @Transactional(readOnly = true)
    public List<AlunoOficinaDTO> listarAlunosOficina() {
        return alunoOficinaRepository.findAll().stream()
            .map(oficinaMapper::toAlunoOficinaDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public AlunoOficinaDTO buscarAlunoOficinaPorId(String id) {
        AlunoOficina aluno = alunoOficinaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscrição não encontrada"));
        
        return oficinaMapper.toAlunoOficinaDTO(aluno);
    }
    
    @Transactional(readOnly = true)
    public List<AlunoOficinaDTO> listarAlunosPorMatricula(String matriculaId) {
        return alunoOficinaRepository.findByMatriculaId(matriculaId).stream()
            .map(oficinaMapper::toAlunoOficinaDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<AlunoOficinaDTO> listarAlunosPorOficina(String oficinaId) {
        return alunoOficinaRepository.findByOficinaId(oficinaId).stream()
            .map(oficinaMapper::toAlunoOficinaDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public AlunoOficinaDTO atualizarInscricao(String id, AlunoOficinaDTO dto) {
        if (!alunoOficinaRepository.existsById(id)) {
            throw new EntityNotFoundException("Inscrição não encontrada");
        }
        
        dto.setId(id);
        AlunoOficina aluno = oficinaMapper.toAlunoOficinaEntity(dto);
        AlunoOficina alunoSalvo = alunoOficinaRepository.save(aluno);
        
        return oficinaMapper.toAlunoOficinaDTO(alunoSalvo);
    }
    
    @Transactional
    public void cancelarInscricao(String id) {
        AlunoOficina aluno = alunoOficinaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Inscrição não encontrada"));
        
        // Atualizar status para cancelado
        aluno.setStatus(AlunoOficina.StatusInscricao.CANCELADO);
        alunoOficinaRepository.save(aluno);
        
        // Liberar vaga na oficina
        Oficina oficina = oficinaRepository.findById(aluno.getOficinaId())
            .orElseThrow(() -> new EntityNotFoundException("Oficina não encontrada"));
        
        oficina.setVagasDisponiveis(oficina.getVagasDisponiveis() + 1);
        oficinaRepository.save(oficina);
    }
}
