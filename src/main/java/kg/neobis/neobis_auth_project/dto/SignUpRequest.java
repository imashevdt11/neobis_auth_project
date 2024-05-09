package kg.neobis.neobis_auth_project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$", message = "Email should contain only letters and digits before @")
    String email;

    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Username should contain only letters")
    @Size(min = 3, max = 30)
    String username;

    @NotBlank(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character (@#$%^&+=), and length between 8 and 15"
    )
    String password;
}
