package pl.put.cmsbackend.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.put.cmsbackend.auth.user.app.AppUserService;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AppUserService appUserService;


    @PostMapping("/register")
    @ResponseStatus(CREATED)
    public Long registerUser(@RequestParam String email, @RequestParam String password) {
        return appUserService.registerUser(email, password).getId();
    }

}
