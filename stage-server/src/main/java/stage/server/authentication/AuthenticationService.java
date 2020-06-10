package stage.server.authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import stage.common.model.*;

@Service
public class AuthenticationService {
    public Role getCurrentRole() {
        String username = (String) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
        User user = new User(); // UserService.getUser(username);
        user.setUsername(username);
        return user.getRole();
    }

    public void requireAdmin() {
        if (getCurrentRole() != Role.ADMIN) {
            throw new ForbiddenException();
        }
    }
}
