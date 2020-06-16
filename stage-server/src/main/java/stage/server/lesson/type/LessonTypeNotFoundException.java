package stage.server.lesson.type;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "The lessonType does not exist.")
public class LessonTypeNotFoundException extends RuntimeException {

    public LessonTypeNotFoundException() { super(); }
}
