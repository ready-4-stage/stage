package stage.server.room;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The room does not exist.")
public class RoomNotFoundException extends RuntimeException {
    public RoomNotFoundException() {
        super();
    }
}
