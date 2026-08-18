package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.AlunoOficinaDTO;
import com.example.matricula.domain.entity.*;
import com.example.matricula.domain.mapper.OficinaMapper;
import com.example.matricula.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OficinaServiceInscricaoTest {
    @Mock OficinaRepository oficinaRepository;
    @Mock ResponsavelRepository responsavelRepository;
    @Mock AgentesCidadaniaRepository agentesCidadaniaRepository;
    @Mock AlunoOficinaRepository alunoOficinaRepository;
    @Mock ParticipanteAvulsoRepository participanteAvulsoRepository;
    @Mock HorarioRepository horarioRepository;
    @Mock OficinaMapper oficinaMapper;
    @InjectMocks OficinaService service;

    private AlunoOficinaDTO dto;
    private Horario horario;
    private Oficina oficina;

    @BeforeEach
    void preparar() {
        dto = new AlunoOficinaDTO();
        dto.setMatriculaId("matricula-1");
        dto.setNomeCompleto("Aluno Teste");
        dto.setOficinaId("oficina-1");
        dto.setHorarioId("horario-1");
        dto.setDataInscricao(LocalDateTime.now());
        dto.setStatus("confirmado");
        horario = new Horario();
        horario.setId("horario-1");
        horario.setVagas(2);
        oficina = new Oficina();
        oficina.setId("oficina-1");
        oficina.setHorarios(List.of(horario));
        lenient().when(oficinaRepository.findById("oficina-1")).thenReturn(Optional.of(oficina));
        lenient().when(horarioRepository.findByIdForUpdate("horario-1")).thenReturn(Optional.of(horario));
    }

    @Test
    void deveInscreverQuandoHorarioTemVaga() {
        AlunoOficina entity = new AlunoOficina();
        when(alunoOficinaRepository.countByHorarioIdAndStatus("horario-1", AlunoOficina.StatusInscricao.CONFIRMADO)).thenReturn(1L);
        when(oficinaMapper.toAlunoOficinaEntity(dto)).thenReturn(entity);
        when(alunoOficinaRepository.save(entity)).thenReturn(entity);
        when(oficinaMapper.toAlunoOficinaDTO(entity)).thenReturn(dto);

        assertSame(dto, service.inscreverAluno(dto));
        verify(horarioRepository).findByIdForUpdate("horario-1");
        verify(alunoOficinaRepository).save(entity);
    }

    @Test
    void deveRecusarHorarioLotado() {
        when(alunoOficinaRepository.countByHorarioIdAndStatus("horario-1", AlunoOficina.StatusInscricao.CONFIRMADO)).thenReturn(2L);
        IllegalStateException erro = assertThrows(IllegalStateException.class, () -> service.inscreverAluno(dto));
        assertTrue(erro.getMessage().contains("vagas"));
        verify(alunoOficinaRepository, never()).save(any());
    }

    @Test
    void deveRecusarInscricaoDuplicada() {
        when(alunoOficinaRepository.existsByMatriculaIdAndHorarioIdAndStatusNot(
            eq("matricula-1"), eq("horario-1"), any())).thenReturn(true);
        assertThrows(IllegalStateException.class, () -> service.inscreverAluno(dto));
        verify(alunoOficinaRepository, never()).save(any());
    }

    @Test
    void deveRecusarHorarioDeOutraOficina() {
        oficina.setHorarios(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.inscreverAluno(dto));
    }
}
