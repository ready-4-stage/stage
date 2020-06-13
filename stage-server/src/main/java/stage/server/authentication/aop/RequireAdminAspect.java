package stage.server.authentication.aop;

import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stage.server.authentication.AuthenticationService;

/**
 * The {@link RequireAdminAspect} joins a method annotated with {@link
 * RequireAdmin} with an execution that takes place before the method
 * execution.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Aspect
@Component
public class RequireAdminAspect {
    private final AuthenticationService authentication;

    @Autowired
    public RequireAdminAspect(AuthenticationService authentication) {
        this.authentication = authentication;
    }

    @Before("@annotation(stage.server.authentication.aop.RequireAdmin)")
    public void requireAdmin() {
        authentication.requireAdmin();
    }
}
