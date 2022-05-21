package pl.put.cmsbackend.auth.token;

import com.auth0.jwt.JWT;
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
import pl.put.cmsbackend.auth.user.AppUser;
import pl.put.cmsbackend.auth.user.AppUserService;
import pl.put.cmsbackend.auth.user.UserNotFoundException;
import pl.put.cmsbackend.auth.user.role.Role;

import java.util.Date;
import java.util.List;

import static pl.put.cmsbackend.auth.config.WebSecurityConfig.ROLE_CLAIM;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private final EncryptionConfig encryptionConfig;
    private final AppUserService appUserService;

    public AuthTokens createTokens(User user, String issuer) {
        return createTokens(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), issuer);
    }

    public AuthTokens refreshTokens(String authorizationHeader, String issuer) {
        DecodedJWT decodedJWT = verifyToken(authorizationHeader);

        try {
            AppUser user = retrieveAppUser(decodedJWT);
            return createTokens(user.getEmail(), getAuthorities(user), issuer);
        } catch (UserNotFoundException e) {
            log.error("{}", e.getMessage());
            throw new InvalidAuthenticationTokenException();
        }

    }


    public UsernamePasswordAuthenticationToken getAuthenticationTokenFromAuthorizationHeader(
            String authorizationHeader) {
        DecodedJWT decodedJWT = verifyToken(authorizationHeader);

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
                .sign(encryptionConfig.getAlgorithm());


        String refreshToken = JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(currentTimeMillis + encryptionConfig.getRefreshTokenExpirationMillis()))
                .withIssuer(issuer)
                .sign(encryptionConfig.getAlgorithm());

        return new AuthTokens(accessToken, refreshToken);
    }

    private DecodedJWT verifyToken(String authorizationHeader) {

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("Invalid authorization header: {}", authorizationHeader);
            throw new InvalidAuthenticationTokenException();
        }

        String JWT = authorizationHeader.substring(7);

        try {
            return encryptionConfig.getJwtVerifier().verify(JWT);
        } catch (JWTVerificationException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            throw new InvalidAuthenticationTokenException();
        }
    }

    private AppUser retrieveAppUser(DecodedJWT decodedJWT) {
        return appUserService.findUserByEmail(decodedJWT.getSubject())
                .orElseThrow(
                        () -> new UserNotFoundException("User with email: " + decodedJWT.getSubject() + "not found"));
    }

    private List<String> getAuthorities(AppUser user) {
        return user.getRoles().stream().map(Role::getName).toList();
    }


}
