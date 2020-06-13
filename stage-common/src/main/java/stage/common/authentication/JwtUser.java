package stage.common.authentication;

/**
 * The {@link JwtUser} is a user that can be used for jwt authorization. It has
 * a username and a password.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
public interface JwtUser {
    String getUsername();

    String getPassword();
}
