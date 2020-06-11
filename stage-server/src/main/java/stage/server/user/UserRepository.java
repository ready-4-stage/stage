package stage.server.user;

import java.sql.SQLException;
import java.util.List;
import stage.common.model.User;
import stage.server.Repository;

/**
 * // TODO description
 *
 * @author Julian Drees, Tobias Fuchs, Yannick Kirschen, Cevin Steve Oehne,
 * Tobias Tappert
 * @since 1.0.0
 */
public interface UserRepository extends Repository {
    List<User> getUsers();

    User getUser(Integer id);

    Integer addUser(User user);

    Integer addUser(User user, Integer id);

    void updateUser(Integer id, User newUser);

    void deleteUser(Integer id);

    Integer getId(String userName);

    boolean isUniqueUserName(String userName);

    Integer generateId() throws SQLException;
}
