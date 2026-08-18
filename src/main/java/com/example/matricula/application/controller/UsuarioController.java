package com.example.matricula.application.controller;

import com.example.matricula.domain.dto.UsuarioRequestDTO;
import com.example.matricula.domain.dto.UsuarioResponseDTO;
import com.example.matricula.domain.entity.Usuario;
import com.example.matricula.domain.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @GetMapping
    public List<UsuarioResponseDTO> listar() { return service.listar(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO criar(@Valid @RequestBody UsuarioRequestDTO dto) { return service.criar(dto); }

    @PutMapping("/{id}")
    public UsuarioResponseDTO atualizar(@PathVariable String id, @Valid @RequestBody UsuarioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable String id, @AuthenticationPrincipal Usuario autenticado) {
        service.excluir(id, autenticado.getId());
    }
}
