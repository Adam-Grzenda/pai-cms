package pl.put.cmsbackend.auth.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.put.cmsbackend.auth.token.InvalidAuthenticationTokenException;
import pl.put.cmsbackend.auth.token.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pl.put.cmsbackend.auth.config.WebSecurityConfig.LOGIN_URI;
import static pl.put.cmsbackend.auth.config.WebSecurityConfig.REFRESH;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if (request.getServletPath().equals(LOGIN_URI) || request.getServletPath().equals(REFRESH)||authorizationHeader == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken = tokenService.getAuthenticationTokenFromAuthorizationHeader(
                    authorizationHeader);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (InvalidAuthenticationTokenException e) {
            log.error("JWT authorization failed: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization token");
        }
    }
}
