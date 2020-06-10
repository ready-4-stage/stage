package stage.server.authentication;

import java.util.LinkedList;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import stage.common.authentication.*;

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
            passwordEncoder().encode(user.getPassword()), new LinkedList<>());
    }

    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
