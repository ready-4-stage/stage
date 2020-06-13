package stage.server.authentication;

import java.util.*;

import org.springframework.stereotype.Service;

/**
 * The {@link TokenCacheService} caches the tokens for a user.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Service
class TokenCacheService {
    private final Map<String, String> tokenCache = new HashMap<>();

    void addToken(String token, String username) {
        tokenCache.put(token, username);
    }

    String getUsernameForToken(String token) {
        return tokenCache.get(token);
    }

    void removeToken(String token) {
        tokenCache.remove(token);
    }
}
