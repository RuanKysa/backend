package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.DashboardResumoDTO;
import com.example.matricula.domain.entity.*;
import com.example.matricula.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final MatriculaRepository matriculaRepository;
    private final OficinaRepository oficinaRepository;
    private final AlunoOficinaRepository alunoOficinaRepository;
    private final AulaRepository aulaRepository;
    private final PresencaRepository presencaRepository;

    @Transactional(readOnly = true)
    public DashboardResumoDTO resumo() {
        LocalDate hoje = LocalDate.now();
        long registros = presencaRepository.countByDataAula(hoje);
        long presentes = presencaRepository.countByDataAulaAndStatus(hoje, Presenca.StatusPresenca.PRESENTE);
        double frequencia = registros == 0 ? 0 : Math.round(presentes * 1000.0 / registros) / 10.0;
        return new DashboardResumoDTO(
            hoje,
            matriculaRepository.count(),
            matriculaRepository.countByStatus(Matricula.StatusMatricula.PENDENTE),
            oficinaRepository.countByStatus(Oficina.StatusOficina.ATIVA),
            alunoOficinaRepository.countByStatus(AlunoOficina.StatusInscricao.CONFIRMADO),
            aulaRepository.countByDataAula(hoje),
            presentes,
            registros,
            frequencia
        );
    }
}
