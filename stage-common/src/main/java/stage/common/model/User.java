package stage.common.model;

import javax.validation.constraints.*;

import lombok.*;
import stage.common.authentication.JwtUser;

/**
 * According to the ERD, the {@link User} represents a human being working with
 * the software. It may be an admin, teacher ({@link Teacher} or student ({@link
 * Student}).
 * <p>
 * The {@link User} has some basic attributes that are enough to perform a
 * proper user management.
 * <p>
 * Internally, a {@link User} is identified via its ID. Hence, the ID must be
 * unique. But an ID is not human-friendly. Thus, externally a {@link User} is
 * identified via its username, which can be a random alphanumeric string (not a
 * numeric one!). The username is also unique but must not be used as a primary
 * key because it may change over time. The ID must not change.
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
public class User implements JwtUser {
    @NotNull(message = "The ID must not be null.")
    private Integer id;

    @NotBlank(message = "The username must not be blank.")
    private String username;

    @NotBlank(message = "The password must not be blank.")
    private String password;

    @NotBlank(message = "The mail must not be blank.")
    private String mail;

    @NotNull(message = "The role must not be null.")
    private Role role;

    public User(User user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        mail = user.getMail();
        role = user.getRole();
    }
}
