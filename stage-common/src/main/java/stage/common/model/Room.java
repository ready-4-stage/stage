package stage.common.model;

import javax.validation.constraints.*;

import lombok.*;

/**
 * According to the ERD, a {@link Room} represents a real classroom that can be
 * booked for a {@link Lesson}.
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
public class Room {
    @NotNull(message = "The ID must not be null.")
    private Integer id;

    @NotBlank(message = "The name must not be blank.")
    private String name;

    @NotBlank(message = "The suitability must not be blank.")
    private String suitability;
}
