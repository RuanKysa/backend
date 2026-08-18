package com.example.matricula.infrastructure.config;

import com.example.matricula.domain.entity.Usuario;
import com.example.matricula.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialAdminConfig implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(InitialAdminConfig.class);
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.initial-admin.name:Administrador}") private String nome;
    @Value("${app.initial-admin.email:admin@sistema.local}") private String email;
    @Value("${app.initial-admin.password:}") private String senha;

    @Override
    public void run(ApplicationArguments args) {
        if (repository.existsByPerfil(Usuario.Perfil.ADMIN)) return;
        if (senha == null || senha.length() < 8) {
            log.warn("Nenhum administrador existe. Defina INITIAL_ADMIN_PASSWORD (mínimo 8 caracteres) e reinicie a aplicação.");
            return;
        }
        Usuario admin = new Usuario();
        admin.setNome(nome);
        admin.setEmail(email);
        admin.setSenhaHash(passwordEncoder.encode(senha));
        admin.setPerfil(Usuario.Perfil.ADMIN);
        admin.setStatus(Usuario.StatusUsuario.ATIVO);
        repository.save(admin);
        log.info("Administrador inicial criado para o e-mail configurado.");
    }
}
