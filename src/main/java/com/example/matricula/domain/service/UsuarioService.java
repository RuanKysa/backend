package com.example.matricula.domain.service;

import com.example.matricula.domain.dto.UsuarioRequestDTO;
import com.example.matricula.domain.dto.UsuarioResponseDTO;
import com.example.matricula.domain.entity.Usuario;
import com.example.matricula.domain.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listar() {
        return repository.findAll().stream().map(UsuarioResponseDTO::from).toList();
    }

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        if (repository.existsByEmailIgnoreCase(dto.email())) {
            throw new IllegalArgumentException("Já existe um usuário com este e-mail");
        }
        if (dto.senha() == null || dto.senha().length() < 8) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 8 caracteres");
        }
        Usuario usuario = new Usuario();
        aplicar(usuario, dto, true);
        return UsuarioResponseDTO.from(repository.save(usuario));
    }

    @Transactional
    public UsuarioResponseDTO atualizar(String id, UsuarioRequestDTO dto) {
        Usuario usuario = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        repository.findByEmailIgnoreCase(dto.email())
            .filter(outro -> !outro.getId().equals(id))
            .ifPresent(outro -> { throw new IllegalArgumentException("Já existe um usuário com este e-mail"); });
        aplicar(usuario, dto, false);
        return UsuarioResponseDTO.from(repository.save(usuario));
    }

    @Transactional
    public void excluir(String id, String usuarioAutenticadoId) {
        if (id.equals(usuarioAutenticadoId)) {
            throw new IllegalArgumentException("Você não pode excluir o próprio usuário");
        }
        if (!repository.existsById(id)) throw new EntityNotFoundException("Usuário não encontrado");
        repository.deleteById(id);
    }

    private void aplicar(Usuario usuario, UsuarioRequestDTO dto, boolean senhaObrigatoria) {
        usuario.setNome(dto.nome().trim());
        usuario.setEmail(dto.email().trim().toLowerCase());
        usuario.setPerfil(Usuario.Perfil.valueOf(dto.perfil().toUpperCase()));
        usuario.setStatus(Usuario.StatusUsuario.valueOf(dto.status().toUpperCase()));
        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));
        } else if (senhaObrigatoria) {
            throw new IllegalArgumentException("Senha obrigatória");
        }
    }
}
