package pl.put.cmsbackend.auth.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionControllerHandler {

    @ExceptionHandler
    public ResponseEntity<UserRegistrationErrorResponse> handleUserRegistrationException(UserRegistrationException e) {
        return ResponseEntity.badRequest().body(new UserRegistrationErrorResponse(e.getMessage()));
    }

    private record UserRegistrationErrorResponse(String message) {

    }

}
