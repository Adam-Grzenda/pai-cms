package pl.put.cmsbackend.auth.user.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String email) {
        super("User with email: " + email + " not found");
    }
}
