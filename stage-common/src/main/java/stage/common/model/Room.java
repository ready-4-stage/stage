package stage.common.model;

import javax.validation.constraints.*;

import lombok.*;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
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
