package com.example.matricula.infrastructure.security;

import com.example.matricula.domain.entity.Usuario;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {
    private final byte[] secret;
    private final long expirationSeconds;
    private final ObjectMapper objectMapper;

    public JwtService(@Value("${app.auth.jwt-secret}") String secret,
                      @Value("${app.auth.token-expiration-hours:8}") long expirationHours,
                      ObjectMapper objectMapper) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET deve possuir pelo menos 32 caracteres");
        }
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationHours * 3600;
        this.objectMapper = objectMapper;
    }

    public String gerar(Usuario usuario) {
        try {
            Map<String, Object> claims = new LinkedHashMap<>();
            claims.put("sub", usuario.getId());
            claims.put("email", usuario.getEmail());
            claims.put("perfil", usuario.getPerfil().name());
            claims.put("exp", Instant.now().getEpochSecond() + expirationSeconds);
            String payload = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(objectMapper.writeValueAsBytes(claims));
            return payload + "." + assinar(payload);
        } catch (Exception e) {
            throw new IllegalStateException("Não foi possível gerar o token", e);
        }
    }

    public Claims validar(String token) {
        try {
            String[] partes = token.split("\\.");
            if (partes.length != 2 || !constante(partes[1], assinar(partes[0]))) return null;
            Map<String, Object> map = objectMapper.readValue(
                Base64.getUrlDecoder().decode(partes[0]), new TypeReference<>() {});
            long exp = ((Number) map.get("exp")).longValue();
            if (exp <= Instant.now().getEpochSecond()) return null;
            return new Claims((String) map.get("sub"), (String) map.get("perfil"));
        } catch (Exception e) {
            return null;
        }
    }

    private String assinar(String valor) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret, "HmacSHA256"));
        return Base64.getUrlEncoder().withoutPadding()
            .encodeToString(mac.doFinal(valor.getBytes(StandardCharsets.UTF_8)));
    }

    private boolean constante(String a, String b) {
        return java.security.MessageDigest.isEqual(
            a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }

    public record Claims(String usuarioId, String perfil) {}
}
