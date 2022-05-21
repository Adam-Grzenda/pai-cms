package pl.put.cmsbackend.auth.user.role;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static pl.put.cmsbackend.auth.config.WebSecurityConfig.DEFAULT_ROLE;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role getDefaultRole() {
        Optional<Role> defaultRole = roleRepository.findRoleByName(DEFAULT_ROLE);
        return defaultRole.orElseGet(() -> roleRepository.save(new Role(DEFAULT_ROLE)));
    }


}
