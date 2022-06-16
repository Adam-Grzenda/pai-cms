package pl.put.cmsbackend.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.put.cmsbackend.auth.token.InvalidAuthenticationTokenException;
import pl.put.cmsbackend.auth.user.exception.InvalidIssuerException;
import pl.put.cmsbackend.auth.user.exception.UserNotFoundException;
import pl.put.cmsbackend.content.exception.ContentAccessPermissionException;

@ControllerAdvice
public class AuthExceptionControllerHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({InvalidAuthenticationTokenException.class})
    private ResponseEntity<AuthErrorResponse> handleException(InvalidAuthenticationTokenException tokenException) {
        return ResponseEntity.badRequest().body(new AuthErrorResponse("Invalid authentication token"));
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<AuthErrorResponse> handleException(UserNotFoundException userNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new AuthErrorResponse(userNotFoundException.getMessage()));
    }

    @ExceptionHandler({ContentAccessPermissionException.class})
    private ResponseEntity<AuthErrorResponse> handleException(
            ContentAccessPermissionException contentAccessPermissionException) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new AuthErrorResponse("User not permitted to access resource"));
    }

    @ExceptionHandler({InvalidIssuerException.class})
    private ResponseEntity<AuthErrorResponse> handleException(InvalidIssuerException e) {
        return ResponseEntity.badRequest().body(new AuthErrorResponse("Invalid reset password token issuer"));
    }


    private record AuthErrorResponse(String message) {

    }
}


