package kg.neobis.neobis_auth_project.exception;

public class UserAlreadyExistsException extends BaseException {
    public UserAlreadyExistsException(String message, Integer status) {
        super(message, status);
    }
}
