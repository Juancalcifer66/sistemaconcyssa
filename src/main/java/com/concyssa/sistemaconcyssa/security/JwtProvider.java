package com.concyssa.sistemaconcyssa.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    // Clave ampliada a más de 64 caracteres (512+ bits) para cumplir con el estándar HS512 de JJWT
    private final String secret = "ClaveSecretaSuperSeguraExtremadamenteLargaParaSistemaConcyssa2026_HS512_Key_OK";
    private final int jwtExpirationMs = 86400000; // 24 horas
    private final Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

    public String generarToken(Authentication authentication) {
        String username = authentication.getName();
        
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            System.out.println(">>> Error JWT: Firma o estructura invalida -> " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println(">>> Error JWT: El token ha expirado -> " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println(">>> Error JWT: Token no soportado -> " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(">>> Error JWT: El token esta vacio o es nulo -> " + e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("roles", List.class);
    }

    public Key getKey() {
        return key;
    }
}
