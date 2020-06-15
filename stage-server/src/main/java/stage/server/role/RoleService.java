package stage.server.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stage.common.model.Role;
import stage.server.authentication.aop.RequireAnybody;

@Service
public class RoleService {
    private final RoleRepository repository;

    @Autowired
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @RequireAnybody
    public Role getRole(Integer id) {
        return repository.getRole(id);
    }

    @RequireAnybody
    public Integer getRoleId(Role role) {
        return repository.getRoleId(role);
    }
}
