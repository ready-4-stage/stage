package stage.server.user;

import java.util.List;
import stage.common.model.User;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface UserRepository {
    List<User> getUsers();

    <T extends User> T getUser(Integer id);

    Integer addUser(User user);

    void updateUser(Integer id, User newUser);

    void deleteUser(Integer id);

    Integer getId(String userName);

    boolean isUniqueUsername(String username);

    Integer generateId();

    default <T extends User> T getUser(String userName) {
        return getUser(getId(userName));
    }

    default void updateUser(String userName, User newUser) {
        updateUser(getId(userName), newUser);
    }

    default void deleteUser(String userName) {
        deleteUser(getId(userName));
    }
}
