package com.example.matricula.infrastructure.security;

import com.example.matricula.domain.entity.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private static final String SECRET = "segredo-de-teste-com-mais-de-32-caracteres";

    @Test
    void deveGerarEValidarTokenAssinado() {
        JwtService service = new JwtService(SECRET, 8, new ObjectMapper());
        Usuario usuario = usuario();

        JwtService.Claims claims = service.validar(service.gerar(usuario));

        assertNotNull(claims);
        assertEquals("usuario-1", claims.usuarioId());
        assertEquals("ADMIN", claims.perfil());
    }

    @Test
    void deveRejeitarTokenAlterado() {
        JwtService service = new JwtService(SECRET, 8, new ObjectMapper());
        String token = service.gerar(usuario());
        String alterado = (token.charAt(0) == 'A' ? "B" : "A") + token.substring(1);

        assertNull(service.validar(alterado));
    }

    @Test
    void deveRejeitarTokenExpirado() {
        JwtService service = new JwtService(SECRET, 0, new ObjectMapper());

        assertNull(service.validar(service.gerar(usuario())));
    }

    @Test
    void deveExigirSegredoForte() {
        assertThrows(IllegalStateException.class,
            () -> new JwtService("curto", 8, new ObjectMapper()));
    }

    private Usuario usuario() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-1");
        usuario.setEmail("admin@sistema.local");
        usuario.setPerfil(Usuario.Perfil.ADMIN);
        return usuario;
    }
}
