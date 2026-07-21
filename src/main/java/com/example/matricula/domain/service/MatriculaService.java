package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.FiltrosMatriculaDTO;
import com.example.matricula.domain.dto.MatriculaRequestDTO;
import com.example.matricula.domain.dto.MatriculaResponseDTO;
import com.example.matricula.domain.entity.Matricula;
import com.example.matricula.domain.mapper.MatriculaMapper;
import com.example.matricula.domain.repository.MatriculaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatriculaService {
    
    private final MatriculaRepository matriculaRepository;
    private final MatriculaMapper matriculaMapper;
    
    /**
     * Criar nova matrícula
     */
    @Transactional
    public MatriculaResponseDTO criarMatricula(MatriculaRequestDTO requestDTO) {
        Matricula matricula = matriculaMapper.toEntity(requestDTO);
        Matricula matriculaSalva = matriculaRepository.save(matricula);
        return matriculaMapper.toResponseDTO(matriculaSalva);
    }
    
    /**
     * Buscar todas as matrículas
     */
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> listarTodas() {
        return matriculaRepository.findAll()
            .stream()
            .map(matriculaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar matrícula por ID
     */
    @Transactional(readOnly = true)
    public MatriculaResponseDTO buscarPorId(String id) {
        Matricula matricula = matriculaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada com ID: " + id));
        
        return matriculaMapper.toResponseDTO(matricula);
    }
    
    /**
     * Atualizar matrícula existente
     */
    @Transactional
    public MatriculaResponseDTO atualizarMatricula(String id, MatriculaRequestDTO requestDTO) {
        Matricula matriculaExistente = matriculaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada com ID: " + id));
        
        matriculaMapper.updateEntityFromDTO(requestDTO, matriculaExistente);
        Matricula matriculaAtualizada = matriculaRepository.save(matriculaExistente);
        
        return matriculaMapper.toResponseDTO(matriculaAtualizada);
    }
    
    /**
     * Deletar matrícula
     */
    @Transactional
    public void deletarMatricula(String id) {
        if (!matriculaRepository.existsById(id)) {
            throw new EntityNotFoundException("Matrícula não encontrada com ID: " + id);
        }
        
        matriculaRepository.deleteById(id);
    }
    
    /**
     * Buscar matrículas com filtros
     */
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarComFiltros(FiltrosMatriculaDTO filtros) {
        LocalDateTime dataInicio = null;
        LocalDateTime dataFim = null;
        Matricula.StatusMatricula status = null;
        
        // Converter datas
        if (filtros.getDataInicio() != null && !filtros.getDataInicio().isEmpty()) {
            dataInicio = LocalDate.parse(filtros.getDataInicio()).atStartOfDay();
        }
        
        if (filtros.getDataFim() != null && !filtros.getDataFim().isEmpty()) {
            dataFim = LocalDate.parse(filtros.getDataFim()).atTime(LocalTime.MAX);
        }
        
        // Converter status
        if (filtros.getStatus() != null && !filtros.getStatus().isEmpty()) {
            try {
                status = Matricula.StatusMatricula.valueOf(filtros.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Ignorar status inválido
            }
        }
        
        List<Matricula> matriculas = matriculaRepository.findComFiltros(
            filtros.getBusca(),
            status,
            dataInicio,
            dataFim
        );
        
        return matriculas.stream()
            .map(matriculaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar por nome
     */
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarPorNome(String nome) {
        return matriculaRepository.findByNomeCompletoContainingIgnoreCase(nome)
            .stream()
            .map(matriculaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar por CPF
     */
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarPorCpf(String cpf) {
        return matriculaRepository.findByCpf(cpf)
            .stream()
            .map(matriculaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Buscar por status
     */
    @Transactional(readOnly = true)
    public List<MatriculaResponseDTO> buscarPorStatus(String statusStr) {
        Matricula.StatusMatricula status = Matricula.StatusMatricula.valueOf(statusStr.toUpperCase());
        
        return matriculaRepository.findByStatus(status)
            .stream()
            .map(matriculaMapper::toResponseDTO)
            .collect(Collectors.toList());
    }
    
    /**
     * Atualizar status da matrícula
     */
    @Transactional
    public MatriculaResponseDTO atualizarStatus(String id, String novoStatus) {
        Matricula matricula = matriculaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada com ID: " + id));
        
        Matricula.StatusMatricula status = Matricula.StatusMatricula.valueOf(novoStatus.toUpperCase());
        matricula.setStatus(status);
        
        Matricula matriculaAtualizada = matriculaRepository.save(matricula);
        return matriculaMapper.toResponseDTO(matriculaAtualizada);
    }
    
    /**
     * Assinar matrícula
     */
    @Transactional
    public MatriculaResponseDTO assinarMatricula(String id, String assinatura) {
        Matricula matricula = matriculaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Matrícula não encontrada com ID: " + id));
        
        matricula.setAssinatura(assinatura);
        matricula.setMatriculaAssinada(true);
        
        Matricula matriculaAtualizada = matriculaRepository.save(matricula);
        return matriculaMapper.toResponseDTO(matriculaAtualizada);
    }
}
