package pl.put.cmsbackend.auth.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.config.EncryptionConfig;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.auth.user.role.Role;
import pl.put.cmsbackend.content.exception.ContentAccessPermissionException;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static pl.put.cmsbackend.auth.config.WebSecurityConfig.ROLE_CLAIM;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final EncryptionConfig encryptionConfig;
    private final AppUserService appUserService;

    public AuthTokens createTokens(User user, String issuer) {
        return createTokens(user.getUsername(), user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList(), issuer);
    }

    public AuthTokens refreshTokens(String authorizationHeader, String issuer) {
        DecodedJWT decodedJWT = verifyAuthorizationHeader(authorizationHeader);

        try {
            AppUser user = retrieveAppUser(decodedJWT);
            return createTokens(user.getEmail(), getAuthorities(user), issuer);
        } catch (UserNotFoundException e) {
            log.error("{}", e.getMessage());
            throw new InvalidAuthenticationTokenException();
        }

    }

    public String generateForgottenPasswordToken(AppUser user, String issuer) {
        long currentTimeMillis = System.currentTimeMillis();
        Algorithm resetPasswordAlgorithm = Algorithm.HMAC256(user.getPassword());

        return JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getAccessTokenExpirationMillis()))
                .withIssuer(issuer)
                .sign(resetPasswordAlgorithm);
    }

    public UsernamePasswordAuthenticationToken getAuthenticationTokenFromAuthorizationHeader(String authorizationHeader) {
        DecodedJWT decodedJWT = verifyAuthorizationHeader(authorizationHeader);

        List<String> claims = decodedJWT.getClaim(ROLE_CLAIM).asList(String.class);


        if (claims != null) {
            List<SimpleGrantedAuthority> authorities = claims.stream().map(SimpleGrantedAuthority::new).toList();
            return new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, authorities);
        } else {
            return new UsernamePasswordAuthenticationToken(decodedJWT.getSubject(), null, List.of());
        }

    }

    private AuthTokens createTokens(String subject, List<String> authorities, String issuer) {
        long currentTimeMillis = System.currentTimeMillis();

        String accessToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getAccessTokenExpirationMillis()))
                .withIssuer(issuer)
                .withClaim(ROLE_CLAIM, authorities)
                .sign(encryptionConfig.getAuthTokenAlgorithm());


        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getRefreshTokenExpirationMillis()))
                .withIssuer(issuer)
                .sign(encryptionConfig.getAuthTokenAlgorithm());

        return new AuthTokens(accessToken, refreshToken);
    }

    private DecodedJWT verifyAuthorizationHeader(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("Invalid authorization header: {}", authorizationHeader);
            throw new InvalidAuthenticationTokenException();
        }

        String JWT = authorizationHeader.substring(7);
        return verifyJwt(JWT);
    }

    private DecodedJWT verifyJwt(String JWT) {
        try {
            return encryptionConfig.getAuthTokenVerifier().verify(JWT);
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            throw new InvalidAuthenticationTokenException();
        }
    }


    private AppUser retrieveAppUser(DecodedJWT decodedJWT) {
        return appUserService.findUserByEmail(decodedJWT.getSubject())
                .orElseThrow(() -> new UserNotFoundException("User with email: " + decodedJWT.getSubject() + "not found"));
    }

    private List<String> getAuthorities(AppUser user) {
        return user.getRoles().stream().map(Role::getName).toList();
    }


    public void verifyResetPasswordToken(AppUser user, String token, String expectedIssuer) {
        Algorithm resetPasswordAlgorithm = Algorithm.HMAC256(user.getPassword());
        JWTVerifier verifier = JWT.require(resetPasswordAlgorithm).withIssuer(expectedIssuer).build();

        try {
            DecodedJWT decodedToken = verifier.verify(token);
            if (!user.getEmail().equals(decodedToken.getSubject())) {
                throw new InvalidAuthenticationTokenException();
            }

        } catch (JWTVerificationException e) {
            throw new InvalidAuthenticationTokenException();
        }
    }

    public void verifyContentToken(String token, @NotNull Long contentId) {
        DecodedJWT decodedJWT = verifyJwt(token);

        if (!decodedJWT.getSubject().equals(contentId.toString())) {
            throw new ContentAccessPermissionException();
        }

    }

    public String generateContentToken(@NotNull Long contentId, Long validForDays) {
        if (validForDays > encryptionConfig.getMaxShareContentTokenValidDays()) {
            throw new ContentAccessPermissionException("Token validity exceeds maximum");
        }

        long currentTimeMillis = System.currentTimeMillis();

        return JWT.create()
                .withSubject(contentId.toString())
                .withExpiresAt(new Date(currentTimeMillis + TimeUnit.DAYS.toMillis(validForDays)))
                .withIssuer("/share")
                .sign(encryptionConfig.getAuthTokenAlgorithm());
    }
}
