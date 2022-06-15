package pl.put.cmsbackend.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import pl.put.cmsbackend.auth.filter.JwtAuthenticationFilter;
import pl.put.cmsbackend.auth.filter.JwtTokenProviderFilter;
import pl.put.cmsbackend.auth.token.TokenService;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_CLAIM = "ROLES";
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String DEFAULT_ROLE = "USER";
    public static final String LOGIN_URI = "/login";
    public static final String API_BASE = "/api/**";
    public static final String REFRESH = "/refresh";
    private static final String CHECK_USER = "/user";

    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = getCorsConfiguration();
        http.cors().configurationSource(request -> corsConfiguration);
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
        http.authorizeRequests().antMatchers(REFRESH).permitAll();
        http.authorizeRequests().antMatchers(CHECK_USER).permitAll();
        http.authorizeRequests().antMatchers(API_BASE).authenticated();
        http.authorizeRequests().antMatchers(POST, API_BASE).hasAnyAuthority(DEFAULT_ROLE);
        http.authorizeRequests().antMatchers(GET, API_BASE).hasAnyAuthority(DEFAULT_ROLE);

        http.addFilter(new JwtTokenProviderFilter(super.authenticationManagerBean(), tokenService, objectMapper));
        http.addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

    }

    private CorsConfiguration getCorsConfiguration() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowedOriginPatterns(List.of(CorsConfiguration.ALL)); //todo - add configuration
        return corsConfiguration;
    }


}
