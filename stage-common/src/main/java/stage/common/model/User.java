package stage.common.model;

import javax.validation.constraints.*;

import lombok.*;
import stage.common.authentication.JwtUser;

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
