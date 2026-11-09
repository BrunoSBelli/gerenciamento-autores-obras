package com.raposo.gerenciador_autores_obras.business.service;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.UsuarioEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.ConflictException;
import com.raposo.gerenciador_autores_obras.infrastructure.exceptions.UnauthorizedException;
import com.raposo.gerenciador_autores_obras.infrastructure.record.TokenResponseDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.record.UsuarioRequestDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.record.UsuarioResponseDTO;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.UsuarioRepository;
import com.raposo.gerenciador_autores_obras.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;           // bean do SecurityConfig
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new ConflictException("E-mail já cadastrado");
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setEmail(dto.email());
        // CRIPTOGRAFA antes de salvar
        usuario.setSenha(passwordEncoder.encode(dto.senha()));

        UsuarioEntity salvo = usuarioRepository.save(usuario);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getEmail());
    }

    public TokenResponseDTO login(UsuarioRequestDTO dto) {
        try {
            // O Spring compara a senha digitada com o hash do banco
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.senha()));

            return new TokenResponseDTO(jwtUtil.generateToken(auth.getName()));
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Usuário ou senha inválidos", e);
        }
    }
}
