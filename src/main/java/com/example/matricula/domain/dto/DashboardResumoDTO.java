package com.example.matricula.domain.dto;

import java.time.LocalDate;

public record DashboardResumoDTO(
    LocalDate dataReferencia,
    long totalMatriculas,
    long matriculasPendentes,
    long oficinasAtivas,
    long inscricoesAtivas,
    long aulasHoje,
    long presencasHoje,
    long registrosChamadaHoje,
    double frequenciaHoje
) {}
