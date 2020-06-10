package stage.server.authentication;

import java.util.*;

import org.springframework.stereotype.Service;

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
