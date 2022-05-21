package pl.put.cmsbackend.auth.config;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = false)
@Getter
public class EncryptionConfig {

    private final Long accessTokenExpirationMillis;
    private final Long refreshTokenExpirationMillis;
    private final Algorithm algorithm;
    private final PasswordEncoder passwordEncoder;

    public EncryptionConfig(String secret, Long accessTokenExpirationMillis, Long refreshTokenExpirationMillis) {
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.algorithm = Algorithm.HMAC256(secret);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

}
