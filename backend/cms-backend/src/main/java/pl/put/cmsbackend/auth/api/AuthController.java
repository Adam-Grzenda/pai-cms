package pl.put.cmsbackend.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.put.cmsbackend.auth.token.AuthTokens;
import pl.put.cmsbackend.auth.token.TokenService;
import pl.put.cmsbackend.auth.user.AppUserService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AppUserService appUserService;
    private final TokenService tokenService;

    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public Long registerUser(@RequestParam String email, @RequestParam String password) {
        return appUserService.registerUser(email, password).getId();
    }

    @PostMapping("/refresh")
    public AuthTokens refreshTokens(@RequestHeader("Authorization") String refreshToken) {
        return tokenService.refreshTokens(refreshToken);
    }


}
