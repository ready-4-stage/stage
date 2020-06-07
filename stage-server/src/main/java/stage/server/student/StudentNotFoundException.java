package stage.server.student;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The user does not exist.")
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException() {
        super();
    }
}
