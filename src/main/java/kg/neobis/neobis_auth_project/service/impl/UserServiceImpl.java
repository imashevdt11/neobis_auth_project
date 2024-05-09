package kg.neobis.neobis_auth_project.service.impl;

import kg.neobis.neobis_auth_project.dto.LogInRequest;
import kg.neobis.neobis_auth_project.dto.LogInResponse;
import kg.neobis.neobis_auth_project.dto.SignUpRequest;
import kg.neobis.neobis_auth_project.entity.ConfirmationToken;
import kg.neobis.neobis_auth_project.entity.User;
import kg.neobis.neobis_auth_project.exception.UserAlreadyExistsException;
import kg.neobis.neobis_auth_project.exception.UserNotFoundException;
import kg.neobis.neobis_auth_project.exception.UserNotVerifiedException;
import kg.neobis.neobis_auth_project.repository.ConfirmationTokenRepository;
import kg.neobis.neobis_auth_project.repository.UserRepository;
import kg.neobis.neobis_auth_project.service.EmailService;
import kg.neobis.neobis_auth_project.service.JwtService;
import kg.neobis.neobis_auth_project.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    AuthenticationManager authenticationManager;

    ConfirmationTokenRepository confirmationTokenRepository;

    EmailService emailService;

    JwtService jwtService;

    PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    @Override
    public LogInResponse logIn(LogInRequest request) {

        var user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Invalid username or password", HttpStatus.NOT_FOUND.value()));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid username or password", HttpStatus.NOT_FOUND.value());
        }

        if (!user.isEnabled()) {
            ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(user);
            confirmationToken.setToken(UUID.randomUUID().toString());
            confirmationToken.setCreated_date(new Date());
            confirmationToken.setExpiry_date(ConfirmationToken.calculateExpiryDate());
            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage simpleMailMessage = emailService.createMail(user, confirmationToken);
            emailService.sendEmail(simpleMailMessage);
            throw new UserNotVerifiedException("Your account is not verified. Verification link has been sent to your email.", HttpStatus.FORBIDDEN.value());
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        return LogInResponse.builder()
                .token(jwtToken)
                .build();
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

    @Override
    public ResponseEntity<?> signUp(SignUpRequest request) {

        if (userRepository.existsUserByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Error: Email is already in use!", HttpStatus.CONFLICT.value());
        }

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .build();

        user = userRepository.save(user);

        ConfirmationToken confirmationToken = generateConfirmationToken(user);
        SimpleMailMessage simpleMailMessage = emailService.createMail(user, confirmationToken);
        emailService.sendEmail(simpleMailMessage);

        return ResponseEntity.ok("Verify email by the link sent on your email address");
    }

    public ConfirmationToken generateConfirmationToken(User user) {

        ConfirmationToken confirmationToken = new ConfirmationToken();
        String token = UUID.randomUUID().toString();
        confirmationToken.setToken(token);
        confirmationToken.setCreated_date(new Date());
        confirmationToken.setUser(user);
        confirmationTokenRepository.save(confirmationToken);

        return confirmationToken;
    }
}
