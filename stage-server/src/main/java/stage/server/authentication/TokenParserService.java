package stage.server.authentication;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * The {@link TokenParserService} allows parsing a token and getting the
 * username out of it. It is intended to be used by every endpoint in order to
 * get the authenticated username. Please keep in mind that the exceptions are
 * only catch in {@link JwtAuthorizationFilter}, since we don't need it in the
 * public endpoints.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Service
public class TokenParserService {
    private final TokenProperties tokenProperties;

    @Autowired
    TokenParserService(TokenProperties tokenProperties) {
        this.tokenProperties = tokenProperties;
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(
            tokenProperties.getSecret().getBytes()).build().parseClaimsJws(
            token.replace("Bearer ", ""));
    }
}
