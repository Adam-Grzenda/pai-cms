package pl.put.cmsbackend.auth.user.app;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.exception.UserRegistrationException;
import pl.put.cmsbackend.auth.user.role.Role;
import pl.put.cmsbackend.auth.user.role.RoleService;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    public AppUser registerUser(String email, String password) {
        validateEmailAndPassword(email, password);

        Role role = roleService.getDefaultRole();
        if (appUserRepository.findUserByEmail(email).isPresent()) {
            throw new UserRegistrationException("User with email: " + email + " already exists");
        }

        return appUserRepository.save(new AppUser(email, passwordEncoder.encode(password), List.of(role)));
    }

    private void validateEmailAndPassword(String email, String password) {
        boolean emailValid = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                .matcher(email)
                .matches();
        boolean passwordValid = password.length() >= 8;

        if (!emailValid || !passwordValid) {
            throw new UserRegistrationException("Invalid email or password format");
        }
    }

    public Optional<AppUser> findUserByEmail(String email) {
        return this.appUserRepository.findUserByEmail(email);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = this.appUserRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + "not found"));

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(), appUser.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList());
    }
}
