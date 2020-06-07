package stage.common.model;

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
public class User {
    private Integer id;
    private String username;
    private String password;
    private String mail;
    private Role role;

    public User(User user) {
        id = user.getId();
        username = user.getUsername();
        password = user.getPassword();
        mail = user.getMail();
        role = user.getRole();
    }
}
