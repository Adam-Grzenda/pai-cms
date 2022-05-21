package pl.put.cmsbackend.auth.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.put.cmsbackend.auth.user.role.Role;
import pl.put.cmsbackend.auth.user.role.RoleService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AppUser registerUser(String email, String password) {
        Role role = roleService.getDefaultRole();
        return appUserRepository.save(new AppUser(email, passwordEncoder.encode(password), List.of(role)));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = this.appUserRepository.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username: " + username + "not found"));

        return new org.springframework.security.core.userdetails.User(appUser.getEmail(), appUser.getPassword(),
                appUser.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList());
    }
}
