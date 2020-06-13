package stage.common.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.*;

import lombok.*;

/**
 * According to the ERD, the {@link Lesson} is represents a single and unique
 * music lesson. A lesson is defined by a time it takes place, a {@link Room}, a
 * {@link Teacher} that gives the lesson and at least one {@link Student} taking
 * part.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @NotNull(message = "The ID must not be null.")
    private Integer id;

    @NotNull(message = "The begin must not be null.")
    private LocalDateTime begin;

    @NotNull(message = "The end must not be null.")
    private LocalDateTime end;

    @NotNull(message = "The room must not be null.")
    private Room room;

    @NotNull(message = "The teacher must not be null.")
    private Teacher teacher;

    @NotNull(message = "The students must not be null.")
    @NotEmpty(message = "The students must not be empty.")
    private List<Student> students;

    @NotNull(message = "The type must not be null.")
    private LessonType type;

    @NotBlank(message = "The content must not be blank.")
    private String content;
}
