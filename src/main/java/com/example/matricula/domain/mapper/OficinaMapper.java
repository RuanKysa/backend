package com.example.matricula.domain.mapper;

import com.example.matricula.domain.dto.*;
import com.example.matricula.domain.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OficinaMapper {
    
    // ========== OFICINA ==========
    
    public Oficina toEntity(OficinaRequestDTO dto) {
        if (dto == null) return null;
        
        Oficina oficina = new Oficina();
        oficina.setNome(dto.getNome());
        oficina.setDescricao(dto.getDescricao());
        oficina.setCategoria(normalizarCategoria(dto.getCategoria()));
        oficina.setIdadeMinima(dto.getIdadeMinima());
        oficina.setIdadeMaxima(dto.getIdadeMaxima());
        oficina.setVagasTotais(dto.getVagasTotais());
        oficina.setVagasDisponiveis(dto.getVagasTotais()); // Inicialmente, todas as vagas estão disponíveis
        oficina.setDataInicio(dto.getDataInicio());
        oficina.setDataFim(dto.getDataFim());
        oficina.setLocal(dto.getLocal());
        oficina.setSala(dto.getSala());
        oficina.setMateriais(dto.getMateriais() != null ? new ArrayList<>(dto.getMateriais()) : new ArrayList<>());
        oficina.setStatus(parseStatusOficina(dto.getStatus()));
        oficina.setCriadoPor(dto.getCriadoPor());
        
        // Converter horários
        if (dto.getHorarios() != null) {
            List<Horario> horarios = dto.getHorarios().stream()
                .map(this::toHorarioEntity)
                .collect(Collectors.toList());
            oficina.setHorarios(horarios);
        }
        
        return oficina;
    }
    
    public OficinaResponseDTO toResponseDTO(Oficina entity) {
        if (entity == null) return null;
        
        OficinaResponseDTO dto = new OficinaResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDescricao(entity.getDescricao());
        dto.setCategoria(normalizarCategoriaSaida(entity.getCategoria()));
        dto.setResponsavel(toResponsavelDTO(entity.getResponsavel()));
        dto.setResponsavelId(entity.getResponsavel() != null ? entity.getResponsavel().getId() : null);
        dto.setIdadeMinima(entity.getIdadeMinima());
        dto.setIdadeMaxima(entity.getIdadeMaxima());
        dto.setVagasTotais(entity.getVagasTotais());
        dto.setVagasDisponiveis(entity.getVagasDisponiveis());
        dto.setDataInicio(entity.getDataInicio());
        dto.setDataFim(entity.getDataFim());
        dto.setLocal(entity.getLocal());
        dto.setSala(entity.getSala());
        dto.setMateriais(entity.getMateriais() != null ? new ArrayList<>(entity.getMateriais()) : new ArrayList<>());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name().toLowerCase() : null);
        dto.setDataCriacao(entity.getDataCriacao());
        dto.setDataAtualizacao(entity.getDataAtualizacao());
        dto.setCriadoPor(entity.getCriadoPor());
        
        // Converter horários
        if (entity.getHorarios() != null) {
            List<HorarioDTO> horarios = entity.getHorarios().stream()
                .map(this::toHorarioDTO)
                .collect(Collectors.toList());
            dto.setHorarios(horarios);
        }
        
        return dto;
    }
    
    public void updateEntityFromDTO(Oficina entity, OficinaRequestDTO dto) {
        if (entity == null || dto == null) return;
        
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setCategoria(normalizarCategoria(dto.getCategoria()));
        entity.setIdadeMinima(dto.getIdadeMinima());
        entity.setIdadeMaxima(dto.getIdadeMaxima());
        entity.setVagasTotais(dto.getVagasTotais());
        entity.setDataInicio(dto.getDataInicio());
        entity.setDataFim(dto.getDataFim());
        entity.setLocal(dto.getLocal());
        entity.setSala(dto.getSala());
        entity.setMateriais(dto.getMateriais() != null ? new ArrayList<>(dto.getMateriais()) : new ArrayList<>());
        entity.setStatus(parseStatusOficina(dto.getStatus()));
        
        // Atualizar horários
        if (dto.getHorarios() != null) {
            entity.getHorarios().clear();
            List<Horario> novosHorarios = dto.getHorarios().stream()
                .map(this::toHorarioEntity)
                .collect(Collectors.toList());
            entity.getHorarios().addAll(novosHorarios);
        }
    }
    
    // ========== RESPONSAVEL ==========
    
    public Responsavel toResponsavelEntity(ResponsavelDTO dto) {
        if (dto == null) return null;
        
        Responsavel responsavel = new Responsavel();
        responsavel.setId(dto.getId());
        responsavel.setNome(dto.getNome());
        responsavel.setTipo(parseTipoResponsavel(dto.getTipo()));
        responsavel.setEmail(dto.getEmail());
        responsavel.setTelefone(dto.getTelefone());
        responsavel.setEspecialidade(dto.getEspecialidade());
        responsavel.setDataAdmissao(dto.getDataAdmissao());
        responsavel.setAnexoCriminal(dto.getAnexoCriminal());
        
        return responsavel;
    }
    
    public ResponsavelDTO toResponsavelDTO(Responsavel entity) {
        if (entity == null) return null;
        
        ResponsavelDTO dto = new ResponsavelDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setTipo(entity.getTipo() != null ? entity.getTipo().name().toLowerCase() : null);
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setEspecialidade(entity.getEspecialidade());
        dto.setDataAdmissao(entity.getDataAdmissao());
        dto.setAnexoCriminal(entity.getAnexoCriminal());
        
        return dto;
    }
    
    // ========== AGENTES CIDADANIA ==========
    
    public AgentesCidadania toAgentesCidadaniaEntity(AgentesCidadaniaDTO dto) {
        if (dto == null) return null;
        
        AgentesCidadania agente = new AgentesCidadania();
        agente.setId(dto.getId());
        agente.setNome(dto.getNome());
        agente.setDataNascimento(dto.getDataNascimento());
        agente.setCpf(dto.getCpf());
        agente.setEmail(dto.getEmail());
        agente.setTelefone(dto.getTelefone());
        agente.setEndereco(dto.getEndereco());
        agente.setDataInicio(dto.getDataInicio());
        agente.setStatus(parseStatusAgente(dto.getStatus()));
        
        // Não mapeamos oficinaId aqui pois é gerenciado pelo relacionamento ManyToMany
        
        return agente;
    }
    
    public AgentesCidadaniaDTO toAgentesCidadaniaDTO(AgentesCidadania entity) {
        if (entity == null) return null;
        
        AgentesCidadaniaDTO dto = new AgentesCidadaniaDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setCpf(entity.getCpf());
        dto.setEmail(entity.getEmail());
        dto.setTelefone(entity.getTelefone());
        dto.setEndereco(entity.getEndereco());
        dto.setDataInicio(entity.getDataInicio());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name().toLowerCase() : null);
        
        // Mapear TODAS as oficinas vinculadas (ManyToMany)
        if (entity.getOficinas() != null && !entity.getOficinas().isEmpty()) {
            List<String> oficinasIds = entity.getOficinas().stream()
                .map(Oficina::getId)
                .collect(java.util.stream.Collectors.toList());
            dto.setOficinasIds(oficinasIds);
        }
        
        return dto;
    }
    
    // ========== HORARIO ==========
    
    public Horario toHorarioEntity(HorarioDTO dto) {
        if (dto == null) return null;
        
        Horario horario = new Horario();
        horario.setId(dto.getId());
        horario.setDiaSemana(parseDiaSemana(dto.getDiaSemana()));
        horario.setHoraInicio(dto.getHoraInicio());
        horario.setHoraFim(dto.getHoraFim());
        horario.setVagas(dto.getVagas());
        
        return horario;
    }
    
    public HorarioDTO toHorarioDTO(Horario entity) {
        if (entity == null) return null;
        
        HorarioDTO dto = new HorarioDTO();
        dto.setId(entity.getId());
        dto.setDiaSemana(entity.getDiaSemana() != null ? entity.getDiaSemana().name().toLowerCase() : null);
        dto.setHoraInicio(entity.getHoraInicio());
        dto.setHoraFim(entity.getHoraFim());
        dto.setVagas(entity.getVagas());
        
        return dto;
    }
    
    // ========== ALUNO OFICINA ==========
    
    public AlunoOficina toAlunoOficinaEntity(AlunoOficinaDTO dto) {
        if (dto == null) return null;
        
        AlunoOficina aluno = new AlunoOficina();
        aluno.setId(dto.getId());
        aluno.setMatriculaId(dto.getMatriculaId());
        aluno.setParticipanteAvulsoId(dto.getParticipanteAvulsoId());
        aluno.setOrigem(dto.getOrigem() == null
            ? (dto.getMatriculaId() == null ? AlunoOficina.OrigemAluno.AVULSO : AlunoOficina.OrigemAluno.MATRICULA)
            : AlunoOficina.OrigemAluno.valueOf(dto.getOrigem().toUpperCase()));
        aluno.setNomeCompleto(dto.getNomeCompleto());
        aluno.setIdade(dto.getIdade());
        aluno.setTurno(dto.getTurno());
        aluno.setTelefone(dto.getTelefone());
        aluno.setNomeResponsavel(dto.getNomeResponsavel());
        aluno.setObservacoes(dto.getObservacoes());
        aluno.setDataInscricao(dto.getDataInscricao());
        aluno.setOficinaId(dto.getOficinaId());
        aluno.setHorarioId(dto.getHorarioId());
        aluno.setStatus(parseStatusInscricao(dto.getStatus()));
        
        return aluno;
    }
    
    public AlunoOficinaDTO toAlunoOficinaDTO(AlunoOficina entity) {
        if (entity == null) return null;
        
        AlunoOficinaDTO dto = new AlunoOficinaDTO();
        dto.setId(entity.getId());
        dto.setMatriculaId(entity.getMatriculaId());
        dto.setParticipanteAvulsoId(entity.getParticipanteAvulsoId());
        dto.setOrigem(entity.getOrigem() != null ? entity.getOrigem().name().toLowerCase() : "matricula");
        ParticipanteAvulso avulso = entity.getParticipanteAvulso();
        dto.setNomeCompleto(avulso != null ? avulso.getNomeCompleto() : entity.getNomeCompleto());
        dto.setIdade(avulso != null ? avulso.getIdade() : entity.getIdade());
        dto.setTurno(avulso != null ? avulso.getTurno() : entity.getTurno());
        dto.setTelefone(avulso != null ? avulso.getTelefone() : entity.getTelefone());
        dto.setNomeResponsavel(avulso != null ? avulso.getNomeResponsavel() : entity.getNomeResponsavel());
        dto.setObservacoes(avulso != null ? avulso.getObservacoes() : entity.getObservacoes());
        dto.setDataInscricao(entity.getDataInscricao());
        dto.setOficinaId(entity.getOficinaId());
        dto.setHorarioId(entity.getHorarioId());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name().toLowerCase() : null);
        
        return dto;
    }
    
    // ========== PARSERS ==========
    
    private String normalizarCategoria(String categoria) {
        if (categoria == null) return null;
        String categoriaTratada = categoria.trim();
        if (categoriaTratada.isEmpty()) {
            throw new IllegalArgumentException("Categoria é obrigatória");
        }
        return categoriaTratada;
    }

    private String normalizarCategoriaSaida(String categoria) {
        if (categoria == null) return null;
        return switch (categoria.toUpperCase()) {
            case "ESPORTE", "ARTE", "MUSICA", "DANCA", "ARTESANATO", "INFORMATICA", "IDIOMAS", "ARTES_MARCIAIS", "OUTRAS" ->
                categoria.toLowerCase();
            default -> categoria;
        };
    }
    
    private Oficina.StatusOficina parseStatusOficina(String status) {
        if (status == null) return null;
        try {
            return Oficina.StatusOficina.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }
    }
    
    private Responsavel.TipoResponsavel parseTipoResponsavel(String tipo) {
        if (tipo == null) return null;
        try {
            return Responsavel.TipoResponsavel.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de responsável inválido: " + tipo);
        }
    }
    
    private AgentesCidadania.StatusAgente parseStatusAgente(String status) {
        if (status == null) return null;
        try {
            return AgentesCidadania.StatusAgente.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de agente inválido: " + status);
        }
    }
    
    private Horario.DiaSemana parseDiaSemana(String dia) {
        if (dia == null) return null;
        try {
            return Horario.DiaSemana.valueOf(dia.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dia da semana inválido: " + dia);
        }
    }
    
    private AlunoOficina.StatusInscricao parseStatusInscricao(String status) {
        if (status == null) return AlunoOficina.StatusInscricao.CONFIRMADO;
        try {
            return AlunoOficina.StatusInscricao.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status de inscrição inválido: " + status);
        }
    }
}
