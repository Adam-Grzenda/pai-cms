package pl.put.cmsbackend.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoderProvider {

    private final EncryptionConfig encryptionConfig;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return encryptionConfig.getPasswordEncoder();
    }
}
