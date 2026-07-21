package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.MatriculaRequestDTO;
import com.example.matricula.domain.dto.MatriculaResponseDTO;
import com.example.matricula.domain.entity.Matricula;
import com.example.matricula.domain.mapper.MatriculaMapper;
import com.example.matricula.domain.repository.MatriculaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatriculaServiceTest {
    
    @Mock
    private MatriculaRepository matriculaRepository;
    
    @Mock
    private MatriculaMapper matriculaMapper;
    
    @InjectMocks
    private MatriculaService matriculaService;
    
    private MatriculaRequestDTO requestDTO;
    private Matricula matricula;
    private MatriculaResponseDTO responseDTO;
    
    @BeforeEach
    void setUp() {
        // Setup request DTO
        requestDTO = new MatriculaRequestDTO();
        requestDTO.setNomeCompleto("João da Silva");
        requestDTO.setDataNascimento(LocalDate.of(2010, 5, 15));
        requestDTO.setPossuiDeficiencia(false);
        requestDTO.setUtilizaTransporte(true);
        requestDTO.setAlmoco(true);
        
        // Setup entity
        matricula = new Matricula();
        matricula.setId("123");
        matricula.setNomeCompleto("João da Silva");
        matricula.setDataNascimento(LocalDate.of(2010, 5, 15));
        matricula.setPossuiDeficiencia(false);
        matricula.setUtilizaTransporte(true);
        matricula.setAlmoco(true);
        
        // Setup response DTO
        responseDTO = new MatriculaResponseDTO();
        responseDTO.setId("123");
        responseDTO.setNomeCompleto("João da Silva");
        responseDTO.setDataNascimento(LocalDate.of(2010, 5, 15));
    }
    
    @Test
    void deveCriarMatriculaComSucesso() {
        // Arrange
        when(matriculaMapper.toEntity(requestDTO)).thenReturn(matricula);
        when(matriculaRepository.save(matricula)).thenReturn(matricula);
        when(matriculaMapper.toResponseDTO(matricula)).thenReturn(responseDTO);
        
        // Act
        MatriculaResponseDTO resultado = matriculaService.criarMatricula(requestDTO);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("123", resultado.getId());
        assertEquals("João da Silva", resultado.getNomeCompleto());
        
        verify(matriculaMapper).toEntity(requestDTO);
        verify(matriculaRepository).save(matricula);
        verify(matriculaMapper).toResponseDTO(matricula);
    }
    
    @Test
    void deveListarTodasAsMatriculas() {
        // Arrange
        List<Matricula> matriculas = Arrays.asList(matricula);
        when(matriculaRepository.findAll()).thenReturn(matriculas);
        when(matriculaMapper.toResponseDTO(any(Matricula.class))).thenReturn(responseDTO);
        
        // Act
        List<MatriculaResponseDTO> resultado = matriculaService.listarTodas();
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        verify(matriculaRepository).findAll();
    }
    
    @Test
    void deveBuscarMatriculaPorId() {
        // Arrange
        when(matriculaRepository.findById("123")).thenReturn(Optional.of(matricula));
        when(matriculaMapper.toResponseDTO(matricula)).thenReturn(responseDTO);
        
        // Act
        MatriculaResponseDTO resultado = matriculaService.buscarPorId("123");
        
        // Assert
        assertNotNull(resultado);
        assertEquals("123", resultado.getId());
        
        verify(matriculaRepository).findById("123");
        verify(matriculaMapper).toResponseDTO(matricula);
    }
    
    @Test
    void deveLancarExcecaoQuandoMatriculaNaoEncontrada() {
        // Arrange
        when(matriculaRepository.findById("999")).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            matriculaService.buscarPorId("999");
        });
        
        verify(matriculaRepository).findById("999");
    }
    
    @Test
    void deveAtualizarMatriculaComSucesso() {
        // Arrange
        when(matriculaRepository.findById("123")).thenReturn(Optional.of(matricula));
        when(matriculaRepository.save(matricula)).thenReturn(matricula);
        when(matriculaMapper.toResponseDTO(matricula)).thenReturn(responseDTO);
        
        // Act
        MatriculaResponseDTO resultado = matriculaService.atualizarMatricula("123", requestDTO);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("123", resultado.getId());
        
        verify(matriculaRepository).findById("123");
        verify(matriculaMapper).updateEntityFromDTO(requestDTO, matricula);
        verify(matriculaRepository).save(matricula);
    }
    
    @Test
    void deveDeletarMatriculaComSucesso() {
        // Arrange
        when(matriculaRepository.existsById("123")).thenReturn(true);
        doNothing().when(matriculaRepository).deleteById("123");
        
        // Act
        matriculaService.deletarMatricula("123");
        
        // Assert
        verify(matriculaRepository).existsById("123");
        verify(matriculaRepository).deleteById("123");
    }
    
    @Test
    void deveLancarExcecaoAoDeletarMatriculaInexistente() {
        // Arrange
        when(matriculaRepository.existsById("999")).thenReturn(false);
        
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            matriculaService.deletarMatricula("999");
        });
        
        verify(matriculaRepository).existsById("999");
        verify(matriculaRepository, never()).deleteById(any());
    }
}
