package com.example.matricula.application.controller;

import com.example.matricula.domain.entity.ArquivoUpload;
import com.example.matricula.domain.repository.ArquivoUploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/arquivos")
@RequiredArgsConstructor
public class ArquivoUploadController {

    private final ArquivoUploadRepository repository;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("tipo") String tipo) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Arquivo vazio"));
        }

        ArquivoUpload arquivo = new ArquivoUpload();
        arquivo.setNomeArquivo(file.getOriginalFilename() != null ? file.getOriginalFilename() : "arquivo");
        arquivo.setTipoConteudo(file.getContentType() != null ? file.getContentType() : MediaType.APPLICATION_OCTET_STREAM_VALUE);
        arquivo.setCategoria(tipo);
        arquivo.setConteudo(file.getBytes());
        arquivo = repository.save(arquivo);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "filePath", "/api/arquivos/" + arquivo.getId(),
            "fileName", arquivo.getNomeArquivo()
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> visualizar(@PathVariable String id) {
        return repository.findById(id)
            .map(arquivo -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(arquivo.getTipoConteudo()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                    .filename(arquivo.getNomeArquivo(), StandardCharsets.UTF_8)
                    .build().toString())
                .body(arquivo.getConteudo()))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
