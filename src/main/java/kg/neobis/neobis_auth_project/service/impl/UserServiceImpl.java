package kg.neobis.neobis_auth_project.service.impl;

import kg.neobis.neobis_auth_project.dto.RegistrationRequest;
import kg.neobis.neobis_auth_project.entity.ConfirmationToken;
import kg.neobis.neobis_auth_project.entity.User;
import kg.neobis.neobis_auth_project.repository.ConfirmationTokenRepository;
import kg.neobis.neobis_auth_project.repository.UserRepository;
import kg.neobis.neobis_auth_project.service.EmailService;
import kg.neobis.neobis_auth_project.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    ConfirmationTokenRepository confirmationTokenRepository;

    EmailService emailService;

    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> register(RegistrationRequest request) {

        if (userRepository.existsUserByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();

        user = userRepository.save(user);

        ConfirmationToken confirmationToken = new ConfirmationToken();

        String token = UUID.randomUUID().toString();
        confirmationToken.setToken(token);
        confirmationToken.setCreated_date(new Date());
        confirmationToken.setUser(user);

        confirmationTokenRepository.save(confirmationToken);
        System.out.println(confirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                +"http://localhost:8085/confirm-account?token="+confirmationToken.getToken());
        emailService.sendEmail(mailMessage);

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    @Override
    public ResponseEntity<?> confirmEmail(String confirmationToken) {
        ConfirmationToken token = confirmationTokenRepository.findByToken(confirmationToken);

        if(token != null && !token.isExpired())
        {
            User user = userRepository.findUserByEmailIgnoreCase(token.getUser().getEmail());
            user.setEnabled(true);
            userRepository.save(user);
            return ResponseEntity.ok("Email verified successfully!");
        }
        return ResponseEntity.badRequest().body("Error: Couldn't verify email");
    }
}