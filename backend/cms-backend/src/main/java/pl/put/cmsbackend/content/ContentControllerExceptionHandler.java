package pl.put.cmsbackend.content;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.put.cmsbackend.content.text.InvalidTextContentException;

@ControllerAdvice
public class ContentControllerExceptionHandler {

    @ExceptionHandler({InvalidTextContentException.class})
    private ResponseEntity<ContentErrorResponse> handleException(InvalidTextContentException contentException) {
        return ResponseEntity.badRequest().body(new ContentErrorResponse(contentException.getMessage()));
    }

}
