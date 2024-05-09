package kg.neobis.neobis_auth_project.service;

import kg.neobis.neobis_auth_project.dto.LogInRequest;
import kg.neobis.neobis_auth_project.dto.LogInResponse;
import kg.neobis.neobis_auth_project.dto.SignUpRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> confirmEmail(String confirmationToken);

    LogInResponse logIn(LogInRequest request);

    ResponseEntity<?> signUp(SignUpRequest request);
}