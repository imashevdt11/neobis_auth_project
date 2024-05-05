package kg.neobis.neobis_auth_project.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseException extends RuntimeException {

    private Integer status;

    public BaseException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}