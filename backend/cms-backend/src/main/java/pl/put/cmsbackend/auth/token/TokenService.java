package pl.put.cmsbackend.auth.token;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.config.EncryptionConfig;

import java.util.Date;

import static pl.put.cmsbackend.auth.config.WebSecurityConfig.ROLE_CLAIM;


@Service
@RequiredArgsConstructor
public class TokenService {

    private final EncryptionConfig encryptionConfig;

    public AuthTokens createTokens(User user, String issuer) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getAccessTokenExpirationMillis()))
                .withIssuer(issuer)
                .withClaim(ROLE_CLAIM, user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .sign(encryptionConfig.getAlgorithm());


        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getRefreshTokenExpirationMillis()))
                .withIssuer(issuer)
                .sign(encryptionConfig.getAlgorithm());

        return new AuthTokens(accessToken, refreshToken);
    }

    public AuthTokens refreshTokens(String refreshToken) {
        return new AuthTokens("", "");
    }

}
