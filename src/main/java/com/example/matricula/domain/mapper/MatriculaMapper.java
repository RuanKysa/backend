package com.example.matricula.domain.mapper;

import com.example.matricula.domain.dto.DocumentoAnexoDTO;
import com.example.matricula.domain.dto.MatriculaRequestDTO;
import com.example.matricula.domain.dto.MatriculaResponseDTO;
import com.example.matricula.domain.entity.DocumentoAnexo;
import com.example.matricula.domain.entity.Matricula;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatriculaMapper {
    
    public Matricula toEntity(MatriculaRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Matricula matricula = new Matricula();
        
        // DADOS PESSOAIS
        matricula.setNomeCompleto(dto.getNomeCompleto());
        matricula.setDataNascimento(dto.getDataNascimento());
        
        // Calcular idade se não foi fornecida
        if (dto.getIdade() != null) {
            matricula.setIdade(dto.getIdade());
        } else if (dto.getDataNascimento() != null) {
            matricula.setIdade(Period.between(dto.getDataNascimento(), LocalDate.now()).getYears());
        }
        
        matricula.setTurnoSCFV(parseEnum(Matricula.TurnoSCFV.class, dto.getTurnoSCFV()));
        matricula.setNaturalidade(dto.getNaturalidade());
        matricula.setMunicipio(dto.getMunicipio());
        matricula.setUf(dto.getUf());
        matricula.setPais(dto.getPais());
        
        // DOCUMENTOS
        matricula.setRg(dto.getRg());
        matricula.setRgUF(dto.getRgUF());
        matricula.setDataExpedicao(dto.getDataExpedicao());
        matricula.setCpf(dto.getCpf());
        
        // CAD ÚNICO
        matricula.setCadUnicoAluno(dto.getCadUnicoAluno());
        matricula.setCadUnicoResponsavel(dto.getCadUnicoResponsavel());
        
        // ETNIA
        matricula.setEtnia(parseEnum(Matricula.Etnia.class, dto.getEtnia()));
        
        // PROGRAMAS SOCIAIS
        matricula.setProgramasSociais(parseProgramasSociais(dto.getProgramasSociais(), dto.getProgramaSocial()));
        matricula.setProgramaSocialOutros(dto.getProgramaSocialOutros());
        matricula.setQuantasPessoasResidencia(dto.getQuantasPessoasResidencia());
        
        // FILIAÇÃO
        matricula.setNomeMae(dto.getNomeMae());
        matricula.setRgMae(dto.getRgMae());
        matricula.setUfMae(dto.getUfMae());
        matricula.setNomePai(dto.getNomePai());
        matricula.setRgPai(dto.getRgPai());
        matricula.setUfPai(dto.getUfPai());
        
        // RESPONSÁVEL
        matricula.setNomeResponsavel(dto.getNomeResponsavel());
        matricula.setParentesco(dto.getParentesco());
        
        // ENDEREÇO
        matricula.setEndereco(dto.getEndereco());
        matricula.setNumeroEndereco(dto.getNumeroEndereco());
        matricula.setComplemento(dto.getComplemento());
        matricula.setBairro(dto.getBairro());
        matricula.setPostoDeSaude(dto.getPostoDeSaude());
        
        // CONTATO
        matricula.setTelefone(dto.getTelefone());
        matricula.setTelefoneOutro(dto.getTelefoneOutro());
        
        // ESCOLA
        matricula.setEscola(dto.getEscola());
        matricula.setSerie(dto.getSerie());
        matricula.setTurno(parseEnum(Matricula.TurnoEscola.class, dto.getTurno()));
        
        // DEFICIÊNCIA
        matricula.setPossuiDeficiencia(dto.getPossuiDeficiencia());
        matricula.setTipoDeficiencia(parseEnum(Matricula.TipoDeficiencia.class, dto.getTipoDeficiencia()));
        
        // OBSERVAÇÕES
        matricula.setObservacoes(dto.getObservacoes());
        
        // SAÚDE
        matricula.setUsaRemediosControlados(dto.getUsaRemediosControlados());
        matricula.setObservacaoRemedios(dto.getObservacaoRemedios());
        
        // AGENTE CIDADANIA
        matricula.setAgentesCidadania(dto.getAgentesCidadania());
        matricula.setDataInicioAgente(dto.getDataInicioAgente());
        
        // TRANSPORTE E ALIMENTAÇÃO
        matricula.setUtilizaTransporte(dto.getUtilizaTransporte());
        matricula.setLocalEmbarque(dto.getLocalEmbarque());
        matricula.setLocalDesembarque(dto.getLocalDesembarque());
        matricula.setAlmoco(dto.getAlmoco());
        
        // ENCAMINHAMENTO
        matricula.setEncaminhadoPor(dto.getEncaminhadoPor());
        matricula.setDataEncaminhamento(dto.getDataEncaminhamento());
        
        // CONTROLE
        matricula.setStatus(parseEnum(Matricula.StatusMatricula.class, dto.getStatus()));
        matricula.setAssinatura(dto.getAssinatura());
        matricula.setMatriculaAssinada(dto.getMatriculaAssinada() != null ? dto.getMatriculaAssinada() : false);
        
        // DOCUMENTOS
        if (dto.getDocumentos() != null && !dto.getDocumentos().isEmpty()) {
            for (DocumentoAnexoDTO docDTO : dto.getDocumentos()) {
                DocumentoAnexo documento = toDocumentoEntity(docDTO);
                matricula.addDocumento(documento);
            }
        }
        
        return matricula;
    }
    
    public MatriculaResponseDTO toResponseDTO(Matricula entity) {
        if (entity == null) {
            return null;
        }
        
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        
        dto.setId(entity.getId());
        
        // DADOS PESSOAIS
        dto.setNomeCompleto(entity.getNomeCompleto());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setIdade(entity.getIdade());
        dto.setTurnoSCFV(enumToString(entity.getTurnoSCFV()));
        dto.setNaturalidade(entity.getNaturalidade());
        dto.setMunicipio(entity.getMunicipio());
        dto.setUf(entity.getUf());
        dto.setPais(entity.getPais());
        
        // DOCUMENTOS
        dto.setRg(entity.getRg());
        dto.setRgUF(entity.getRgUF());
        dto.setDataExpedicao(entity.getDataExpedicao());
        dto.setCpf(entity.getCpf());
        
        // CAD ÚNICO
        dto.setCadUnicoAluno(entity.getCadUnicoAluno());
        dto.setCadUnicoResponsavel(entity.getCadUnicoResponsavel());
        
        // ETNIA
        dto.setEtnia(enumToString(entity.getEtnia()));
        
        // PROGRAMAS SOCIAIS
        dto.setProgramasSociais(entity.getProgramasSociais().stream().map(this::enumToString).toList());
        dto.setProgramaSocial(dto.getProgramasSociais().stream().findFirst().orElse(null));
        dto.setProgramaSocialOutros(entity.getProgramaSocialOutros());
        dto.setQuantasPessoasResidencia(entity.getQuantasPessoasResidencia());
        
        // FILIAÇÃO
        dto.setNomeMae(entity.getNomeMae());
        dto.setRgMae(entity.getRgMae());
        dto.setUfMae(entity.getUfMae());
        dto.setNomePai(entity.getNomePai());
        dto.setRgPai(entity.getRgPai());
        dto.setUfPai(entity.getUfPai());
        
        // RESPONSÁVEL
        dto.setNomeResponsavel(entity.getNomeResponsavel());
        dto.setParentesco(entity.getParentesco());
        
        // ENDEREÇO
        dto.setEndereco(entity.getEndereco());
        dto.setNumeroEndereco(entity.getNumeroEndereco());
        dto.setComplemento(entity.getComplemento());
        dto.setBairro(entity.getBairro());
        dto.setPostoDeSaude(entity.getPostoDeSaude());
        
        // CONTATO
        dto.setTelefone(entity.getTelefone());
        dto.setTelefoneOutro(entity.getTelefoneOutro());
        
        // ESCOLA
        dto.setEscola(entity.getEscola());
        dto.setSerie(entity.getSerie());
        dto.setTurno(enumToString(entity.getTurno()));
        
        // DEFICIÊNCIA
        dto.setPossuiDeficiencia(entity.getPossuiDeficiencia());
        dto.setTipoDeficiencia(enumToString(entity.getTipoDeficiencia()));
        
        // OBSERVAÇÕES
        dto.setObservacoes(entity.getObservacoes());
        
        // SAÚDE
        dto.setUsaRemediosControlados(entity.getUsaRemediosControlados());
        dto.setObservacaoRemedios(entity.getObservacaoRemedios());
        
        // AGENTE CIDADANIA
        dto.setAgentesCidadania(entity.getAgentesCidadania());
        dto.setDataInicioAgente(entity.getDataInicioAgente());
        
        // TRANSPORTE E ALIMENTAÇÃO
        dto.setUtilizaTransporte(entity.getUtilizaTransporte());
        dto.setLocalEmbarque(entity.getLocalEmbarque());
        dto.setLocalDesembarque(entity.getLocalDesembarque());
        dto.setAlmoco(entity.getAlmoco());
        
        // ENCAMINHAMENTO
        dto.setEncaminhadoPor(entity.getEncaminhadoPor());
        dto.setDataEncaminhamento(entity.getDataEncaminhamento());
        
        // DOCUMENTOS
        if (entity.getDocumentos() != null) {
            dto.setDocumentos(
                entity.getDocumentos().stream()
                    .map(this::toDocumentoDTO)
                    .collect(Collectors.toList())
            );
        }
        
        // CONTROLE
        dto.setStatus(enumToString(entity.getStatus()));
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setDataAtualizacao(entity.getDataAtualizacao());
        dto.setAssinatura(entity.getAssinatura());
        dto.setMatriculaAssinada(entity.getMatriculaAssinada());
        
        return dto;
    }
    
    public DocumentoAnexoDTO toDocumentoDTO(DocumentoAnexo entity) {
        if (entity == null) {
            return null;
        }
        
        DocumentoAnexoDTO dto = new DocumentoAnexoDTO();
        dto.setId(entity.getId());
        dto.setTipo(enumToString(entity.getTipo()));
        dto.setNomeArquivo(entity.getNomeArquivo());
        dto.setUrl(entity.getUrl());
        dto.setDataUpload(entity.getDataUpload());
        
        return dto;
    }
    
    public DocumentoAnexo toDocumentoEntity(DocumentoAnexoDTO dto) {
        if (dto == null) {
            return null;
        }
        
        DocumentoAnexo entity = new DocumentoAnexo();
        entity.setTipo(parseEnum(DocumentoAnexo.TipoDocumento.class, dto.getTipo()));
        entity.setNomeArquivo(dto.getNomeArquivo());
        entity.setUrl(dto.getUrl());
        entity.setDataUpload(dto.getDataUpload());
        
        return entity;
    }
    
    public void updateEntityFromDTO(MatriculaRequestDTO dto, Matricula entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        // Atualiza apenas os campos que não são nulos no DTO
        if (dto.getNomeCompleto() != null) {
            entity.setNomeCompleto(dto.getNomeCompleto());
        }
        
        if (dto.getDataNascimento() != null) {
            entity.setDataNascimento(dto.getDataNascimento());
            entity.setIdade(Period.between(dto.getDataNascimento(), LocalDate.now()).getYears());
        }
        
        if (dto.getTurnoSCFV() != null) {
            entity.setTurnoSCFV(parseEnum(Matricula.TurnoSCFV.class, dto.getTurnoSCFV()));
        }
        
        entity.setNaturalidade(dto.getNaturalidade());
        entity.setMunicipio(dto.getMunicipio());
        entity.setUf(dto.getUf());
        entity.setPais(dto.getPais());
        entity.setRg(dto.getRg());
        entity.setRgUF(dto.getRgUF());
        entity.setDataExpedicao(dto.getDataExpedicao());
        entity.setCpf(dto.getCpf());
        entity.setCadUnicoAluno(dto.getCadUnicoAluno());
        entity.setCadUnicoResponsavel(dto.getCadUnicoResponsavel());
        
        if (dto.getEtnia() != null) {
            entity.setEtnia(parseEnum(Matricula.Etnia.class, dto.getEtnia()));
        }
        
        if (dto.getProgramasSociais() != null || dto.getProgramaSocial() != null) {
            entity.setProgramasSociais(parseProgramasSociais(dto.getProgramasSociais(), dto.getProgramaSocial()));
        }
        entity.setProgramaSocialOutros(dto.getProgramaSocialOutros());
        
        entity.setQuantasPessoasResidencia(dto.getQuantasPessoasResidencia());
        entity.setNomeMae(dto.getNomeMae());
        entity.setRgMae(dto.getRgMae());
        entity.setUfMae(dto.getUfMae());
        entity.setNomePai(dto.getNomePai());
        entity.setRgPai(dto.getRgPai());
        entity.setUfPai(dto.getUfPai());
        entity.setNomeResponsavel(dto.getNomeResponsavel());
        entity.setParentesco(dto.getParentesco());
        entity.setEndereco(dto.getEndereco());
        entity.setNumeroEndereco(dto.getNumeroEndereco());
        entity.setComplemento(dto.getComplemento());
        entity.setBairro(dto.getBairro());
        entity.setPostoDeSaude(dto.getPostoDeSaude());
        entity.setTelefone(dto.getTelefone());
        entity.setTelefoneOutro(dto.getTelefoneOutro());
        entity.setEscola(dto.getEscola());
        entity.setSerie(dto.getSerie());
        
        if (dto.getTurno() != null) {
            entity.setTurno(parseEnum(Matricula.TurnoEscola.class, dto.getTurno()));
        }
        
        if (dto.getPossuiDeficiencia() != null) {
            entity.setPossuiDeficiencia(dto.getPossuiDeficiencia());
        }
        
        if (dto.getTipoDeficiencia() != null) {
            entity.setTipoDeficiencia(parseEnum(Matricula.TipoDeficiencia.class, dto.getTipoDeficiencia()));
        }
        
        entity.setObservacoes(dto.getObservacoes());
        
        // SAÚDE
        entity.setUsaRemediosControlados(dto.getUsaRemediosControlados());
        entity.setObservacaoRemedios(dto.getObservacaoRemedios());
        
        // AGENTE CIDADANIA
        entity.setAgentesCidadania(dto.getAgentesCidadania());
        entity.setDataInicioAgente(dto.getDataInicioAgente());
        
        if (dto.getUtilizaTransporte() != null) {
            entity.setUtilizaTransporte(dto.getUtilizaTransporte());
        }
        
        entity.setLocalEmbarque(dto.getLocalEmbarque());
        entity.setLocalDesembarque(dto.getLocalDesembarque());
        
        if (dto.getAlmoco() != null) {
            entity.setAlmoco(dto.getAlmoco());
        }
        
        entity.setEncaminhadoPor(dto.getEncaminhadoPor());
        entity.setDataEncaminhamento(dto.getDataEncaminhamento());
        
        if (dto.getStatus() != null) {
            entity.setStatus(parseEnum(Matricula.StatusMatricula.class, dto.getStatus()));
        }
        
        entity.setAssinatura(dto.getAssinatura());
        
        if (dto.getMatriculaAssinada() != null) {
            entity.setMatriculaAssinada(dto.getMatriculaAssinada());
        }
        
        // DOCUMENTOS - Atualiza a lista de documentos
        if (dto.getDocumentos() != null) {
            // Limpa os documentos antigos
            entity.getDocumentos().clear();
            
            // Adiciona os novos documentos
            for (DocumentoAnexoDTO docDTO : dto.getDocumentos()) {
                DocumentoAnexo documento = toDocumentoEntity(docDTO);
                entity.addDocumento(documento);
            }
        }
    }
    
    // Métodos auxiliares
    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    private String enumToString(Enum<?> enumValue) {
        return enumValue != null ? enumValue.name().toLowerCase() : null;
    }

    private LinkedHashSet<Matricula.ProgramaSocial> parseProgramasSociais(List<String> valores, String valorLegado) {
        LinkedHashSet<Matricula.ProgramaSocial> programas = new LinkedHashSet<>();
        if (valores != null) {
            valores.stream()
                .map(valor -> parseEnum(Matricula.ProgramaSocial.class, valor))
                .filter(java.util.Objects::nonNull)
                .forEach(programas::add);
        }
        if (programas.isEmpty()) {
            Matricula.ProgramaSocial legado = parseEnum(Matricula.ProgramaSocial.class, valorLegado);
            programas.add(legado != null ? legado : Matricula.ProgramaSocial.NAO_POSSUI);
        }
        if (programas.size() > 1) {
            programas.remove(Matricula.ProgramaSocial.NAO_POSSUI);
        }
        return programas;
    }
}
