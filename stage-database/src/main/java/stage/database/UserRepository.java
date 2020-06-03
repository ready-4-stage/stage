package stage.database;

import stage.common.model.User;

import java.util.List;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne, Tobias Tappert
 * @since 1.0.0
 */
public interface UserRepository {
    List<User> getUsers();

    User getUser(Integer id);

    Integer addUser(User user);

    void updateUser(Integer id, User newUser);

    void deleteUser(Integer id);

    Integer getId(String userName);

    default User getUser(String userName) {
        return getUser(getId(userName));
    }

    default void updateUser(String userName, User newUser) {
        updateUser(getId(userName), newUser);
    }

    default void deleteUser(String userName) {
        deleteUser(getId(userName));
    }
}
