package stage.server.authentication;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
public interface AuthenticationService {
    String login(Integer userId);

    void logout(Integer userId);

    boolean hasValidSession(Integer userId);
}
