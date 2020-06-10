package stage.server.authentication;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.*;
import javax.servlet.http.*;

import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final TokenParserService tokenParserService;
    private final TokenCacheService tokenCacheService;

    JwtAuthorizationFilter(AuthenticationManager authenticationManager,
        TokenParserService tokenParserService,
        TokenCacheService tokenCacheService) {
        super(authenticationManager);
        this.tokenParserService = tokenParserService;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(
            request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(
        HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(token) && token.startsWith("Bearer")) {
            try {
                Jws<Claims> parsedToken = tokenParserService.parseToken(token);

                String username = parsedToken.getBody().getSubject();
                List<GrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                    .get("rol")).stream()
                    .map(authority -> new SimpleGrantedAuthority(
                        (String) authority))
                    .collect(Collectors.toList());

                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username,
                        null, authorities);
                }
            } catch (ExpiredJwtException e) {
                log.warn("Request to parse expired JWT : {} failed : {}", token,
                    e.getMessage());
                tokenCacheService.removeToken(token);
            } catch (UnsupportedJwtException e) {
                log.warn("Request to parse unsupported JWT : {} failed : {}",
                    token, e.getMessage());
                tokenCacheService.removeToken(token);
            } catch (MalformedJwtException e) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token,
                    e.getMessage());
                tokenCacheService.removeToken(token);
            } catch (IllegalArgumentException e) {
                log.warn("Request to parse empty or null JWT : {} failed : {}",
                    token, e.getMessage());
                tokenCacheService.removeToken(token);
            }
        }
        return null;
    }
}
