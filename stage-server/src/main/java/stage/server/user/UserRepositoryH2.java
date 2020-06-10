package stage.server.user;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.FileUtil;
import stage.common.model.User;
import stage.server.database.SqlConnection;
import stage.server.role.RoleRepository;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for {@link stage.common.model.User} data models.
 * Uses H2 sql connection
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

    private final SqlConnection sqlConnection;
    private final RoleRepository roleRepository;

    @Autowired
    public UserRepositoryH2(SqlConnection sqlConnection, RoleRepository roleRepository) {
        this.sqlConnection = sqlConnection;
        this.roleRepository = roleRepository;
        uniqueUserNameSql = FileUtil.readFile("sql/user/user_username_unique.sql");
        generateIdSql = FileUtil.readFile("sql/generate_id.sql");
        deleteUserSql = FileUtil.readFile("sql/user/user_delete.sql");
        selectUserIdSql = FileUtil.readFile("sql/user/user_select_id.sql");
        selectUserByIdSql = FileUtil.readFile("sql/user/user_select_by_id.sql");
        selectAllUsersSql = FileUtil.readFile("sql/user/user_select_all.sql");
        insertUserSql = FileUtil.readFile("sql/user/user_insert.sql");
        updateUserSql = FileUtil.readFile("sql/user/user_update.sql");
    }

    @PostConstruct
    @Override
    public void onInitialize() {
        String createRoleTable = FileUtil.readFile("sql/role/role_table_create.sql");
        String createUserTable = FileUtil.readFile("sql/user/user_table_create.sql");
        try {
            sqlConnection.update(createRoleTable);
            sqlConnection.update(createUserTable);

            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public boolean isUniqueUserName(String userName) {
        try (ResultSet rs = this.sqlConnection.result(uniqueUserNameSql, userName.toUpperCase())) {
            if (rs.next()) {
                return false;
            }
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return true;
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (ResultSet rs = sqlConnection.result(selectAllUsersSql)) {
            while (rs.next()) {
                users.add(buildUser(rs));
            }
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return users;
    }

    @Override
    public User getUser(Integer id) {
        User user = null;
        try (ResultSet resultSet = sqlConnection.result(selectUserByIdSql, id)) {
            if (resultSet.next()) {
                user = buildUser(resultSet);
            }
            sqlConnection.commit();
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
            sqlConnection.update(insertUserSql, id, user.getUsername(),
                user.getPassword(), roleRepository.getRoleId(user.getRole()),
                user.getMail());
            sqlConnection.commit();
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
            sqlConnection.update(updateUserSql, newUser.getUsername(), newUser.getPassword(),
                newUser.getMail(), roleRepository.getRoleId(oldUser.getRole()), id);
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public void deleteUser(Integer id) {
        try {
            sqlConnection.update(deleteUserSql, id);
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Integer getId(String userName) {
        int id = -1;
        try (ResultSet rs = sqlConnection.result(selectUserIdSql, userName.toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }

    @Override
    public Integer generateId() throws SQLException {
        int id = 0;
        try (ResultSet rs = this.sqlConnection.result(generateIdSql)) {
            if (rs.next()) {
                id = rs.getInt(1);
            }
            sqlConnection.commit();
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
