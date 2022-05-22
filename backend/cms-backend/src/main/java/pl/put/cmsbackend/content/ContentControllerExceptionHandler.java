package pl.put.cmsbackend.content;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.put.cmsbackend.content.exception.InvalidTextContentException;
import pl.put.cmsbackend.content.exception.TextContentNotFound;

@ControllerAdvice
public class ContentControllerExceptionHandler {

    @ExceptionHandler({InvalidTextContentException.class})
    private ResponseEntity<ContentErrorResponse> handleException(InvalidTextContentException contentException) {
        return ResponseEntity.badRequest().body(new ContentErrorResponse(contentException.getMessage()));
    }

    @ExceptionHandler({TextContentNotFound.class})
    private ResponseEntity<ContentErrorResponse> handleException(TextContentNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ContentErrorResponse(e.getMessage()));
    }

    private record ContentErrorResponse(String message) {

    }

}
