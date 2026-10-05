package com.concyssa.sistemaconcyssa.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        if ("/error".equals(request.getRequestURI())){
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);

        if (token != null && jwtProvider.validarToken(token)) {
            String username = jwtProvider.getUsernameFromToken(token);
            List<String> roles = jwtProvider.getRolesFromToken(token);

            // Garantiza que cada autoridad tenga el prefijo "ROLE_" que requiere Spring Security para hasRole() / hasAnyRole()
            List<GrantedAuthority> authorities = roles.stream()
                    .map(rol -> rol.startsWith("ROLE_") ? rol : "ROLE_" + rol)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            System.out.println(">>> JwtFilter: Usuario autenticado = " + username + " | Roles = " + authorities + " <<<");

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}