package stage.server.authentication.aop;

import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import stage.server.authentication.AuthenticationService;

/**
 * The {@link RequireAnybodyAspect} joins a method annotated with {@link
 * RequireAnybody} with an execution that takes place before the method
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
public class RequireAnybodyAspect {
    private final AuthenticationService authentication;

    @Autowired
    public RequireAnybodyAspect(AuthenticationService authentication) {
        this.authentication = authentication;
    }

    @Before("@annotation(stage.server.authentication.aop.RequireAdminOrTeacher)")
    public void adminOrTeacher() {
        authentication.requireAnybody();
    }
}
