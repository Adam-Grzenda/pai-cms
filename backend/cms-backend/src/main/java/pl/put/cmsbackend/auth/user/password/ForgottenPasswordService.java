package pl.put.cmsbackend.auth.user.password;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.token.TokenService;
import pl.put.cmsbackend.auth.user.app.AppUser;
import pl.put.cmsbackend.auth.user.app.AppUserRepository;
import pl.put.cmsbackend.auth.user.exception.InvalidIssuerException;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.notification.email.EmailNotification;
import pl.put.cmsbackend.notification.request.NotificationRequest;
import pl.put.cmsbackend.notification.request.NotificationRequestPublisher;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ForgottenPasswordService {

    private static final String FORGOTTEN_PASSWORD_SUBJECT = "Forgotten Password";
    private static final String USERNAME = "username";
    private static final String TOKEN = "token";
    private static final String EXPECTED_ISSUER = "/forgot-password";

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationRequestPublisher notificationRequestPublisher;

    @Value("${mail.template.forgottenPassword}")
    private String forgottenPasswordTemplate;

    public void sendForgottenPassword(String email, String issuer) {

        if (!EXPECTED_ISSUER.equals(issuer)) {
            throw new InvalidIssuerException();
        }

        AppUser user = appUserRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        String token = tokenService.generateForgottenPasswordToken(user, issuer);

        EmailNotification forgottenPasswordNotification = EmailNotification.builder()
                .htmlTemplate(forgottenPasswordTemplate)
                .recipient(email)
                .subject(FORGOTTEN_PASSWORD_SUBJECT)
                .templateVariables(Map.of(USERNAME, email, TOKEN, token))
                .build();


        NotificationRequest notificationRequest = NotificationRequest.builder()
                .notificationType(NotificationRequest.NotificationType.EMAIL)
                .notification(forgottenPasswordNotification)
                .build();

        notificationRequestPublisher.publishRequest(notificationRequest);
    }

    public void handleResetPassword(PasswordResetDto passwordResetDto, String token) {
        AppUser appUser = appUserRepository.findUserByEmail(passwordResetDto.username())
                .orElseThrow(() -> new UsernameNotFoundException(passwordResetDto.username()));

        tokenService. verifyResetPasswordToken(appUser, token, EXPECTED_ISSUER);

        appUser.setPassword(passwordEncoder.encode(passwordResetDto.password()));
        appUserRepository.save(appUser);
    }


}
