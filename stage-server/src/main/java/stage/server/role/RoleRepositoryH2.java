package stage.server.role;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.FileUtil;
import stage.common.model.Role;
import stage.server.database.SqlConnection;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Repository for {@link Role} data models.
 */
@Log4j2
@Service
public class RoleRepositoryH2 implements RoleRepository {

    private final SqlConnection sqlConnection;

    private final String selectRoleByIdSql;
    private final String selectRoleIdSql;

    @Autowired
    public RoleRepositoryH2(SqlConnection sqlConnection) {
        this.sqlConnection = sqlConnection;

        selectRoleByIdSql = FileUtil.readFile("sql/role/role_select_by_id.sql");
        selectRoleIdSql = FileUtil.readFile("sql/role/role_select_id.sql");
    }

    @PostConstruct
    @Override
    public void onInitialize() {
        String createRoleTable = FileUtil.readFile("sql/role/role_table_create.sql");
        try {
            sqlConnection.update(createRoleTable);
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
    }

    @Override
    public Role getRole(Integer id) {
        try (ResultSet rs = sqlConnection.result(selectRoleByIdSql, id)) {
            if (rs.next()) {
                String name = rs.getString("DESCRIPTION").toUpperCase();
                return Role.valueOf(name);
            }
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return null;
    }

    @Override
    public Integer getRoleId(Role role) {
        int id = -1;
        try (ResultSet rs = sqlConnection.result(selectRoleIdSql, role.name().toUpperCase())) {
            if (rs.next()) {
                id = rs.getInt("ID");
            }
            sqlConnection.commit();
        } catch (SQLException ex) {
            log.error(ex);
        }
        return id;
    }
}
