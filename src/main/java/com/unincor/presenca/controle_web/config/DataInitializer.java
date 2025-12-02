package com.unincor.presenca.controle_web.config;

import com.unincor.presenca.controle_web.model.domain.Usuario;
import com.unincor.presenca.controle_web.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j

public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existsByEmail(adminEmail)) {
            Usuario admin = new Usuario();
            admin.setEmail(adminEmail);
            admin.setSenha(passwordEncoder.encode(adminPassword));
            admin.setNome("Administrador");
            admin.setPapel(Usuario.Papel.ADMIN);
            admin.setAtivo(true);

            usuarioRepository.save(admin);
            log.info("✅ Usuário admin criado: {}", adminEmail);
            log.info("🔑 Senha padrão: {}", adminPassword);
        } else {
            log.info("ℹ️ Usuário admin já existe");
        }
    }

}