package com.raposo.gerenciador_autores_obras.infrastructure.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.raposo.gerenciador_autores_obras.infrastructure.record.ErroResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

// Define a classe JwtRequestFilter, que estende OncePerRequestFilter
public class JwtRequestFilter extends OncePerRequestFilter {

    // Criado UMA vez só e reaproveitado em todas as requisições
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // Define propriedades para armazenar instâncias de JwtUtil e UserDetailsService
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    // Construtor que inicializa as propriedades com instâncias fornecidas
    public JwtRequestFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // Metodo chamado uma vez por requisição para processar o filtro
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        try {
            // ... lógica do token igual ...
            chain.doFilter(request, response);

        } catch (ExpiredJwtException e) {            // mais específica: SEMPRE primeiro
            escreverErro(response, request, "Token expirado!");

        } catch (JwtException e) {                   // mais geral: cobre token inválido/adulterado
            escreverErro(response, request, "Token inválido!");
        }
    }

    private void escreverErro(HttpServletResponse response, HttpServletRequest request,
                              String mensagem) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // evita problemas com acentos
        response.getWriter().write(
                buildError(HttpStatus.UNAUTHORIZED.value(), mensagem, request.getRequestURI()));
    }

    private String buildError(int status, String mensagem, String path) {
        ErroResponseDTO erro = ErroResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .message(mensagem)
                .status(status)
                .path(path)
                .build();
        try {
            return OBJECT_MAPPER.writeValueAsString(erro);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
