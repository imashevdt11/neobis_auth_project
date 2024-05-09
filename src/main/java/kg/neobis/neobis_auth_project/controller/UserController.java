package kg.neobis.neobis_auth_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.neobis.neobis_auth_project.dto.LogInRequest;
import kg.neobis.neobis_auth_project.dto.LogInResponse;
import kg.neobis.neobis_auth_project.dto.SignUpRequest;
import kg.neobis.neobis_auth_project.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Validated
@RestController
@AllArgsConstructor
@Tag(name = "User")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @Operation(
            description = "This method performs the user authentication process using his username and password, " +
                    "generates a JWT token and returns a response with a token in case of successful authentication, " +
                    "otherwise throws the appropriate exception.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User with entered data exists. JWT token retrieved"),
                    @ApiResponse(responseCode = "404", description = "Invalid username or password"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/logIn")
    public ResponseEntity<LogInResponse> logIn(@RequestBody LogInRequest request) {
        return ResponseEntity.ok(userService.logIn(request));
    }

    @Operation(
            description = "This method registers a new user based on the data provided in the request, hashes his password, " +
                    "sets the enabled flag to false, saves the user to the database, generates a confirmation token " +
                    "and sends it to the user's email for confirmation. Returns a response with the message " +
                    "\"Verify email by the link sent on your email address\".",
            responses = {
                    @ApiResponse(responseCode = "201", description = "The user's data has been saved in the database. The link to activate the account has been sent by email."),
                    @ApiResponse(responseCode = "400", description = "Bad request / Validation error"),
                    @ApiResponse(responseCode = "409", description = "Entered email is already in use"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation error: " + result.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(request));
    }

    @RequestMapping(value="/confirm-account", method= {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> confirmAccount(@RequestParam("token")String confirmationToken) {
        return userService.confirmEmail(confirmationToken);
    }
}
