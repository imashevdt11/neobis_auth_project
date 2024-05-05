package kg.neobis.neobis_auth_project.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(String message, Integer status) {
        super(message, status);
    }
}