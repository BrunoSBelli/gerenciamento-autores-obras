package com.raposo.gerenciador_autores_obras.infrastructure.security;

import com.raposo.gerenciador_autores_obras.infrastructure.entity.AutorEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.entity.UsuarioEntity;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.AutorRepository;
import com.raposo.gerenciador_autores_obras.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository; // agora busca em Usuario

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getEmail())
                .password(usuario.getSenha()) // hash BCrypt do banco
                .roles("USER")                // papel básico
                .build();
    }
}
