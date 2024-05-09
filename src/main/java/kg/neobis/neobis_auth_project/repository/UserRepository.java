package kg.neobis.neobis_auth_project.repository;

import kg.neobis.neobis_auth_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByEmailIgnoreCase(String email);

    Boolean existsUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}