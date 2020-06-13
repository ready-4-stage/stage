package stage.server.authentication.aop;

import java.lang.annotation.*;

/**
 * Methods annotated with {@link RequireAdminOrTeacher} can only be executed if
 * the currently logged in user is admin or teacher. Otherwise a 403 is
 * returned.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireAdminOrTeacher {
}
