package kg.neobis.neobis_auth_project.service;

import kg.neobis.neobis_auth_project.dto.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> register(RegistrationRequest request);

    ResponseEntity<?> confirmEmail(String confirmationToken);
}
