package com.concyssa.sistemaconcyssa.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String jsonResponse = "{"
                + "\"timestamp\": \"" + LocalDateTime.now() + "\","
                + "\"status\": 403,"
                + "\"error\": \"Forbidden\","
                + "\"message\": \"Acceso denegado: No tienes los permisos requeridos para consultar este recurso.\","
                + "\"path\": \"" + request.getRequestURI() + "\""
                + "}";

        response.getWriter().write(jsonResponse);
    }
}