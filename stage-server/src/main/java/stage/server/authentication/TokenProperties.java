package stage.server.authentication;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The {@link TokenProperties} are the properties for the token as defined in
 * application.yml.
 *
 * @author Julian Drees
 * @author Tobias Fuchs
 * @author Yannick Kirschen
 * @author Cevin Steve Oehne
 * @author Tobias Tappert
 * @since 1.0.0
 */
@Data
@Component
@ConfigurationProperties("token")
public final class TokenProperties {
    private String authenticationUrl;
    private String publicUrl;
    private String secret = RandomStringUtils.randomAlphanumeric(512);
}
