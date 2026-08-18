package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.ChamadaRequestDTO;
import com.example.matricula.domain.dto.PresencaDTO;
import com.example.matricula.domain.entity.*;
import com.example.matricula.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChamadaServiceTest {
    @Mock PresencaRepository presencaRepository;
    @Mock AlunoOficinaRepository alunoOficinaRepository;
    @Mock OficinaRepository oficinaRepository;
    @Mock AulaRepository aulaRepository;
    @InjectMocks ChamadaService service;

    @Test
    void deveCriarAulaEAssociarPresencaAoHorario() {
        Oficina oficina = new Oficina(); oficina.setId("oficina-1");
        AlunoOficina aluno = new AlunoOficina();
        aluno.setId("inscricao-1"); aluno.setOficinaId("oficina-1"); aluno.setHorarioId("horario-1");
        PresencaDTO item = new PresencaDTO();
        item.setAlunoOficinaId("inscricao-1"); item.setStatus(Presenca.StatusPresenca.PRESENTE);
        ChamadaRequestDTO request = new ChamadaRequestDTO();
        request.setDataAula(LocalDate.of(2026, 8, 17));
        request.setHorarioId("horario-1"); request.setPresencas(List.of(item));
        when(oficinaRepository.findById("oficina-1")).thenReturn(Optional.of(oficina));
        when(aulaRepository.findByOficinaIdAndHorarioIdAndDataAula(any(), any(), any())).thenReturn(Optional.empty());
        when(aulaRepository.save(any())).thenAnswer(invocation -> { Aula a = invocation.getArgument(0); a.setId("aula-1"); return a; });
        when(alunoOficinaRepository.findById("inscricao-1")).thenReturn(Optional.of(aluno));
        when(presencaRepository.findByAlunoOficinaIdAndAulaId("inscricao-1", "aula-1")).thenReturn(Optional.empty());
        when(presencaRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        List<Presenca> resultado = service.registrarChamada("oficina-1", request);

        assertEquals(1, resultado.size());
        assertEquals("aula-1", resultado.get(0).getAulaId());
        assertEquals("horario-1", resultado.get(0).getHorarioId());
    }

    @Test
    void deveExigirHorario() {
        when(oficinaRepository.findById("oficina-1")).thenReturn(Optional.of(new Oficina()));
        ChamadaRequestDTO request = new ChamadaRequestDTO();
        request.setDataAula(LocalDate.now()); request.setPresencas(List.of());
        assertThrows(IllegalArgumentException.class, () -> service.registrarChamada("oficina-1", request));
    }
}
