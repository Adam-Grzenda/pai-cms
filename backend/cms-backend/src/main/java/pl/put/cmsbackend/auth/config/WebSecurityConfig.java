package pl.put.cmsbackend.auth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.put.cmsbackend.auth.filter.JwtAuthenticationFilter;
import pl.put.cmsbackend.auth.token.TokenService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_CLAIM = "ROLES";
    public static final String EMAIL_PARAMETER = "email";
    public static final String PASSWORD_PARAMETER = "password";
    public static final String DEFAULT_ROLE = "USER";

    private final UserDetailsService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilter(new JwtAuthenticationFilter(super.authenticationManagerBean(), tokenService, objectMapper));
        http.authorizeRequests().anyRequest().permitAll();
    }


}
