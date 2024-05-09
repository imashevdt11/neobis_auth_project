package kg.neobis.neobis_auth_project.exception;

public class UserNotVerifiedException extends BaseException {
    public UserNotVerifiedException(String message, Integer status) {
        super(message, status);
    }
}
