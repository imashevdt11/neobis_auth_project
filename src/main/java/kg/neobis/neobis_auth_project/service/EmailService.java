package kg.neobis.neobis_auth_project.service;

import kg.neobis.neobis_auth_project.entity.ConfirmationToken;
import kg.neobis.neobis_auth_project.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailService {

    JavaMailSender javaMailSender;

    @Async
    public void sendEmail(SimpleMailMessage email) {
        javaMailSender.send(email);
    }

    public SimpleMailMessage createMail(User user, ConfirmationToken confirmationToken) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Email Verification");
        mailMessage.setText("To confirm your account, please click here : "
                + "https://neobis-auth-project-e28eca5cdfcc.herokuapp.com/confirm-account?token=" + confirmationToken.getToken());

        return mailMessage;
    }
}
