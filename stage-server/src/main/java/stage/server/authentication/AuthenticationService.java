package stage.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import stage.common.model.*;
import stage.server.user.UserService;

/**
 * The {@link AuthenticationService} is the main interface to communicate with,
 * when doing security stuff within the service.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Service
public class AuthenticationService {
    private final UserService userService;

    @Autowired
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @return The role of the currently logged in user.
     */
    public Role getCurrentRole() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);
        user.setUsername(username);
        return user.getRole();
    }

    /**
     * Checks if the currently logged in user is admin. If not, a {@link
     * ForbiddenException} is thrown.
     *
     * @see Role
     */
    public void requireAdmin() {
        if (getCurrentRole() != Role.ADMIN) {
            throw new ForbiddenException();
        }
    }

    /**
     * Checks if the currently logged in user is admin or teacher. If not, a
     * {@link ForbiddenException} is thrown.
     *
     * @see Role
     */
    public void requireAdminOrTeacher() {
        Role role = getCurrentRole();
        if (role != Role.ADMIN && role != Role.TEACHER) {
            throw new ForbiddenException();
        }
    }

    /**
     * Checks if there is a user logged in. If not, a {@link ForbiddenException}
     * is thrown.
     *
     * @see Role
     */
    public void requireAnybody() {
        if (getCurrentRole() == null) {
            throw new ForbiddenException();
        }
    }
}
