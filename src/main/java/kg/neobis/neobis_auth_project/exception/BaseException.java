package kg.neobis.neobis_auth_project.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseException extends RuntimeException {

    private Integer status;

    public BaseException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
