package kg.neobis.neobis_auth_project.repository;

import kg.neobis.neobis_auth_project.entity.ConfirmationToken;
import kg.neobis.neobis_auth_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {
    ConfirmationToken findByToken(String confirmationToken);

    ConfirmationToken findByUser(User user);
}
