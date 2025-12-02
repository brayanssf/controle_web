package com.unincor.presenca.controle_web.model.service;

import com.unincor.presenca.controle_web.controller.dto.AutenticacaoRequest;
import com.unincor.presenca.controle_web.controller.dto.AutenticacaoResponse;
import com.unincor.presenca.controle_web.controller.dto.RegistroRequest;
import com.unincor.presenca.controle_web.model.domain.Usuario;
import com.unincor.presenca.controle_web.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor

public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AutenticacaoResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setNome(request.getNome());
        usuario.setPapel(Usuario.Papel.USUARIO);
        usuario.setAtivo(true);

        usuarioRepository.save(usuario);

        var userDetails = loadUserByUsername(usuario.getEmail());
        String token = jwtService.gerarToken(userDetails, usuario.getPapel().name());

        return new AutenticacaoResponse(
                token,
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getPapel().name()
        );
    }

    @Transactional
    public AutenticacaoResponse autenticar(AutenticacaoRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getSenha()
                )
        );

        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado ou inativo"));

        var userDetails = loadUserByUsername(usuario.getEmail());
        String token = jwtService.gerarToken(userDetails, usuario.getPapel().name());

        return new AutenticacaoResponse(
                token,
                usuario.getEmail(),
                usuario.getNome(),
                usuario.getPapel().name()
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmailAndAtivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getSenha(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getPapel().name()))
        );
    }

}
