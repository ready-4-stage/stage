package stage.server.authentication;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The {@link JwtAuthenticationFilter} manages the authentication of a user. It
 * checks the credentials and creates a token if they're correct.
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
@Log4j2
class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProperties tokenProperties;
    private final AuthenticationManager authenticationManager;
    private final TokenCacheService tokenCacheService;

    JwtAuthenticationFilter(AuthenticationManager authenticationManager,
        TokenProperties tokenProperties, TokenCacheService tokenCacheService) {
        this.authenticationManager = authenticationManager;
        this.tokenProperties = tokenProperties;
        this.tokenCacheService = tokenCacheService;
        setFilterProcessesUrl(tokenProperties.getAuthenticationUrl());
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain,
        Authentication authentication) throws IOException {
        User user = ((User) authentication.getPrincipal());
        List<String> roles = user.getAuthorities().stream().map(
            GrantedAuthority::getAuthority).collect(Collectors.toList());
        byte[] signingKey = tokenProperties.getSecret().getBytes();
        String username = user.getUsername();
        String token = Jwts.builder().signWith(Keys.hmacShaKeyFor(signingKey),
            SignatureAlgorithm.HS512).setHeaderParam("typ", "JWT").setIssuer(
            "secure-api").setAudience("secure-app").setSubject(
            username).setExpiration(
            new Date(System.currentTimeMillis() + 600_000)).claim("rol",
            roles).compact();

        log.info("Logged in user '{}'.", username);
        tokenCacheService.addToken(token, username);

        response.addHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(
            "{" + "   \"token\": \"" + token + "\"" + "}");
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.warn("Unsuccessful authentication.");
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
