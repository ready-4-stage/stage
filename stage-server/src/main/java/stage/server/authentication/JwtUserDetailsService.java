package stage.server.authentication;

import java.util.LinkedList;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.common.authentication.*;

/**
 * The {@link JwtUserDetailsService} is a wrapper for accessing the Database and
 * load the user data.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @implNote The logic has been taken and transformed from
 * https://www.javainuse.com/spring/boot-jwt.
 * @since 1.0.0
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {
    private static JwtUserDatabase database;

    public static void setDatabase(Object clazz) {
        JwtUserDetailsService.database = (JwtUserDatabase) clazz;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        JwtUser user = database.assertAndGet(username);
        return new User(user.getUsername(),
            "{bcrypt}" + passwordEncoder().encode(user.getPassword()),
            new LinkedList<>());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
