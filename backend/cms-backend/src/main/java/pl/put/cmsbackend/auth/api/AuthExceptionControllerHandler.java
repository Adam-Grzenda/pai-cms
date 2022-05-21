package pl.put.cmsbackend.auth.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.put.cmsbackend.auth.token.InvalidAuthenticationTokenException;
import pl.put.cmsbackend.auth.user.UserNotFoundException;
import pl.put.cmsbackend.content.ContentAccessPermissionException;

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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthErrorResponse("User not permitted to access resource"));
    }

    private record AuthErrorResponse(String message) {

    }
}


