package stage.server.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * // TODO description
 *
 * @author Julian Drees
 * @since 15.06.2020
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The user does not exist.")
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super();
    }
}
