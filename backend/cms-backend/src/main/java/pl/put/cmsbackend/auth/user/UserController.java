package pl.put.cmsbackend.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.put.cmsbackend.auth.token.TokenService;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.password.ForgottenPasswordService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AppUserService appUserService;
    private final ForgottenPasswordService forgottenPasswordService;


    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public Long registerUser(@RequestParam String email, @RequestParam String password) {
        return appUserService.registerUser(email, password).getId();
    }

    @PostMapping("/forgot-password/")
    @ResponseStatus(OK)
    public void forgotPassword(@RequestParam String email) {
        forgottenPasswordService.handleForgottenPassword(email);
    }

}
