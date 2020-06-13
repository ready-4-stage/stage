package stage.common.authentication;

/**
 * The {@link JwtUserDatabase} allows accessing the database in order to get the
 * user based on its name.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@FunctionalInterface
public interface JwtUserDatabase {
    JwtUser assertAndGet(String username);
}
