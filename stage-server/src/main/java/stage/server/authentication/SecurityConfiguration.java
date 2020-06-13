package stage.server.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * The {@link SecurityConfiguration} configures the security of the entire REST
 * service. All endpoints under "/v1" are filtered by the {@link
 * JwtAuthorizationFilter}.
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
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final TokenProperties tokenProperties;
    private final TokenParserService tokenParserService;
    private final TokenCacheService tokenCacheService;
    private final JwtUserDetailsService userDetailsService;

    @Autowired
    SecurityConfiguration(TokenParserService tokenParserService,
        JwtUserDetailsService userDetailsService,
        TokenProperties tokenProperties, TokenCacheService tokenCacheService) {
        this.tokenProperties = tokenProperties;
        this.tokenParserService = tokenParserService;
        this.userDetailsService = userDetailsService;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(
            tokenProperties.getPublicUrl()).permitAll().anyRequest().authenticated().and().addFilter(
            new JwtAuthenticationFilter(authenticationManager(),
                tokenProperties, tokenCacheService)).addFilter(
            new JwtAuthorizationFilter(authenticationManager(),
                tokenParserService,
                tokenCacheService)).sessionManagement().sessionCreationPolicy(
            SessionCreationPolicy.STATELESS);
    }

    @Override
    @SuppressWarnings("java:S5344") // Password is encrypted in other class.
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }
}
