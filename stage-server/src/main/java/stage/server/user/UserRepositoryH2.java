package stage.server.user;

import java.sql.*;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import stage.common.FileUtil;
import stage.common.model.*;
import stage.server.database.SqlConnection;

import static stage.common.FileUtil.readFile;

/**
 * Repository for {@link stage.common.model.User} data models. Uses H2 sql
 * connection
 */
@Log4j2
@Service
class UserRepositoryH2 implements UserRepository {
    private final SqlConnection connection;

    private final String uniqueUsername;
    private final String delete;
    private final String selectId;
    private final String selectById;
    private final String select;
    private final String insert;
    private final String update;
    private final String sequence;

    @Autowired
    UserRepositoryH2(SqlConnection connection) {
        this.connection = connection;

        uniqueUsername = FileUtil.readFile("sql/user/username_unique.sql");
        delete = FileUtil.readFile("sql/user/delete.sql");
        selectId = FileUtil.readFile("sql/user/select_id.sql");
        selectById = FileUtil.readFile("sql/user/select_by_id.sql");
        select = FileUtil.readFile("sql/user/select.sql");
        insert = FileUtil.readFile("sql/user/insert.sql");
        update = FileUtil.readFile("sql/user/update.sql");
        sequence = readFile("sql/user/sequence.sql");
    }

    @Override
    public boolean isUniqueUserName(String userName) {
        try (ResultSet rs = connection.result(uniqueUsername,
            userName.toUpperCase())) {
            if (rs.next()) {
                return false;
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return true;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (ResultSet rs = connection.result(select)) {
            while (rs.next()) {
                users.add(buildUser(rs));
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return users;
    }

    @Override
    public User getUser(Integer id) {
        User user = null;
        try (ResultSet resultSet = connection.result(selectById, id)) {
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return user;
    }

    @Override
    public Integer addUser(User user) {
        Integer id = -1;
        try (ResultSet res = connection.result(sequence)) {
            if (res.next()) {
                return addUser(user, id);
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Integer addUser(User user, Integer id) {
        try {
            connection.update(insert, //
                id, //
                user.getUsername(), //
                user.getPassword(), //
                user.getRole().toString(), //
                user.getMail());
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public void updateUser(Integer id, User newUser) {
        User oldUser = getUser(id);
        assert oldUser != null;
        try {
            connection.update(update, //
                newUser.getUsername(), //
                newUser.getPassword(), //
                newUser.getMail(), //
                oldUser.getRole().toString(), //
                id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            connection.update(delete, id);
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Integer getId(String userName) {
        int id = -1;
        try (ResultSet rs = connection.result(selectId,
            userName.toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("ID"));
        user.setUsername(resultSet.getString("USERNAME"));
        user.setPassword(resultSet.getString("PWD"));
        user.setMail(resultSet.getString("MAIL"));
        user.setRole(Role.valueOf(resultSet.getString("ROLE_DESCRIPTION")));
        return user;
    }
}
