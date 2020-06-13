package stage.server.role;

import stage.common.model.Role;

public interface RoleRepository {
    Role getRole(Integer id);

    Integer getRoleId(Role role);
}
