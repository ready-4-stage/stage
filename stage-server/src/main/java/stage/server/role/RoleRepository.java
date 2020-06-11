package stage.server.role;

import stage.common.model.Role;
import stage.server.Repository;

public interface RoleRepository extends Repository {
    Role getRole(Integer id);

    Integer getRoleId(Role role);
}
