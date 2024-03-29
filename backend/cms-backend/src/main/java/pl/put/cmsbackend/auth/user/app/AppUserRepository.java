package pl.put.cmsbackend.auth.user.app;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findUserByEmail(String email);
}
