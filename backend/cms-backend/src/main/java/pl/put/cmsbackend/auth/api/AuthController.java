package pl.put.cmsbackend.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.put.cmsbackend.auth.token.AuthTokens;
import pl.put.cmsbackend.auth.token.TokenService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;


    @PostMapping("/refresh")
    public AuthTokens refreshTokens(@RequestHeader("Authorization") String authorizationHeader,
                                    HttpServletRequest request) {
        return tokenService.refreshTokens(authorizationHeader, request.getRequestURI());
    }

}
