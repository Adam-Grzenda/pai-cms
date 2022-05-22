package pl.put.cmsbackend.auth.user.password;

import javax.validation.constraints.NotBlank;

public record PasswordResetDto(@NotBlank String username, @NotBlank String password) {

}
