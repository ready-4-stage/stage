package stage.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;

@Service
public class TokenParserService {
    private final TokenProperties tokenProperties;

    @Autowired
    TokenParserService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(tokenProperties.getSecret().getBytes())
            .build()
            .parseClaimsJws(token.replace("Bearer ", ""));
    }
}
