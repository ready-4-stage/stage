package stage.server.user;

import java.sql.*;
import java.util.*;
import javax.annotation.PostConstruct;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.FileUtil;
import stage.common.model.User;
import stage.server.database.SqlConnection;
import stage.server.role.RoleRepository;

/**
 * Repository for {@link stage.common.model.User} data models. Uses H2 sql
 * connection
 */
@Log4j2
@Service
public class UserRepositoryH2 implements UserRepository {
    private final String uniqueUserNameSql;
    private final String generateIdSql;
    private final String deleteUserSql;
    private final String selectUserIdSql;
    private final String selectUserByIdSql;
    private final String selectAllUsersSql;
    private final String insertUserSql;
    private final String updateUserSql;

    private final SqlConnection connection;

    // TODO: Replace by service
    private final RoleRepository roleRepository;

    @Autowired
    public UserRepositoryH2(SqlConnection connection,
        RoleRepository roleRepository) {
        this.connection = connection;
        this.roleRepository = roleRepository;
        uniqueUserNameSql = FileUtil.readFile(
            "sql/user/user_username_unique.sql");
        generateIdSql = FileUtil.readFile("sql/generate_id.sql");
        deleteUserSql = FileUtil.readFile("sql/user/user_delete.sql");
        selectUserIdSql = FileUtil.readFile("sql/user/user_select_id.sql");
        selectUserByIdSql = FileUtil.readFile("sql/user/user_select_by_id.sql");
        selectAllUsersSql = FileUtil.readFile("sql/user/user_select_all.sql");
        insertUserSql = FileUtil.readFile("sql/user/user_insert.sql");
        updateUserSql = FileUtil.readFile("sql/user/user_update.sql");
    }

    @PostConstruct
    public void initialize() {
        String createRoleTable = FileUtil.readFile(
            "sql/role/role_table_create.sql");
        String createUserTable = FileUtil.readFile(
            "sql/user/user_table_create.sql");
        try {
            connection.update(createRoleTable);
            connection.update(createUserTable);

            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public boolean isUniqueUserName(String userName) {
        try (ResultSet rs = connection.result(uniqueUserNameSql,
            userName.toUpperCase())) {
            if (rs.next()) {
                return false;
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return true;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (ResultSet rs = connection.result(selectAllUsersSql)) {
            while (rs.next()) {
                users.add(buildUser(rs));
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return users;
    }

    @Override
    public User getUser(Integer id) {
        User user = null;
        try (ResultSet resultSet = connection.result(selectUserByIdSql, id)) {
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return user;
    }

    @Override
    public Integer addUser(User user) {
        Integer id = -1;
        try {
            id = generateId();
            return addUser(user, id);
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Integer addUser(User user, Integer id) {
        try {
            connection.update(insertUserSql, id, user.getUsername(),
                user.getPassword(), roleRepository.getRoleId(user.getRole()),
                user.getMail());
            connection.commit();
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
            connection.update(updateUserSql, newUser.getUsername(),
                newUser.getPassword(), newUser.getMail(),
                roleRepository.getRoleId(oldUser.getRole()), id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            connection.update(deleteUserSql, id);
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Integer getId(String userName) {
        int id = -1;
        try (ResultSet rs = connection.result(selectUserIdSql,
            userName.toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            connection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Integer generateId() throws SQLException {
        int id = 0;
        try (ResultSet rs = connection.result(generateIdSql)) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
            connection.commit();
        }
        return id + 1;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("ID"));
        user.setUsername(resultSet.getString("USERNAME"));
        user.setPassword(resultSet.getString("PASSWORD"));
        user.setMail(resultSet.getString("MAIL"));
        user.setRole(roleRepository.getRole(resultSet.getInt("ROLE_ID")));
        return user;
    }
}
