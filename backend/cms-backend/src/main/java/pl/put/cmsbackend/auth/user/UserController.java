package pl.put.cmsbackend.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.cmsbackend.auth.user.app.AppUserService;
import pl.put.cmsbackend.auth.user.password.ForgottenPasswordService;
import pl.put.cmsbackend.auth.user.password.PasswordResetDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

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

    @PostMapping("/forgot-password")
    @ResponseStatus(OK)
    public void forgotPassword(@RequestParam String email, HttpServletRequest request) {
        forgottenPasswordService.sendForgottenPassword(email, request.getRequestURI());
    }

    @PostMapping("/reset-password")
    @ResponseStatus(OK)
    public void resetPassword(@RequestBody PasswordResetDto passwordResetDto) {
        forgottenPasswordService.handleResetPassword(passwordResetDto);
    }

    @PostMapping("/user")
    public ResponseEntity<Boolean> checkUserExisting(@RequestParam String email) {
        return ResponseEntity.ok(appUserService.findUserByEmail(email).isPresent());
    }

}
