package stage.server.authentication;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("token")
public final class TokenProperties {
    private String authenticationUrl;
    private String publicUrl;
    private String secret;
}
