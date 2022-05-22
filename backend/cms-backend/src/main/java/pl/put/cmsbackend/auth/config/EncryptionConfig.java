package pl.put.cmsbackend.auth.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
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
    private final Algorithm authTokenAlgorithm;
    private final PasswordEncoder passwordEncoder;
    private final JWTVerifier authTokenVerifier;

    private final Long resetPasswordTokenExpirationMillis;

    public EncryptionConfig(String secret, Long accessTokenExpirationMillis, Long refreshTokenExpirationMillis,
                            Long resetPasswordTokenExpirationMillis) {
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
        this.authTokenAlgorithm = Algorithm.HMAC256(secret);
        this.authTokenVerifier = JWT.require(authTokenAlgorithm).build();
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.resetPasswordTokenExpirationMillis = resetPasswordTokenExpirationMillis;
    }

}
