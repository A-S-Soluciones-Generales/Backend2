package com.ays.kardex.config;

import com.ays.kardex.entity.Usuario;
import com.ays.kardex.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedAdmin() {
        return args -> {
            if (!usuarioRepository.existsByRole(Usuario.Role.GLOBAL_ADMIN)) {
                Usuario admin = Usuario.builder()
                        .nombre("Administrador")
                        .apellido("Global")
                        .email("admin@kardex.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Usuario.Role.GLOBAL_ADMIN)
                        .activo(true)
                        .build();
                usuarioRepository.save(admin);
            }
        };
    }
}
