package stage.server.authentication.aop;

import java.lang.annotation.*;

/**
 * Methods annotated with {@link RequireAnybody} can be executed by anyone
 * logged in.
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
public @interface RequireAnybody {
}
