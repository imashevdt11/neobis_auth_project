package kg.neobis.neobis_auth_project.repository;

import kg.neobis.neobis_auth_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmailIgnoreCase(String emailId);

    Boolean existsUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}