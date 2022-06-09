package pl.put.cmsbackend.content.exception;

public class ContentAccessPermissionException extends RuntimeException {
    public ContentAccessPermissionException(String message) {
        super(message);
    }

    public ContentAccessPermissionException() {
        super();
    }
}
