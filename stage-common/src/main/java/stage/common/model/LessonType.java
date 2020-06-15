package stage.common.model;

import javax.validation.constraints.*;

import lombok.*;

/**
 * The {@link LessonType} indicates which type a lesson is of, so that one can
 * see quickly what the lesson is all about.
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
public class LessonType {
    @NotNull(message = "The ID must not be null.")
    private Integer id;

    @NotBlank(message = "The description must not be blank.")
    private String description;
}
