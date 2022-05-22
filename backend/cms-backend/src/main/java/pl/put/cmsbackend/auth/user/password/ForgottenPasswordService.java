package pl.put.cmsbackend.auth.user.password;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.app.AppUserRepository;

@Service
@RequiredArgsConstructor
public class ForgottenPasswordService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;



    public void handleForgottenPassword(String email) {

    }

}
