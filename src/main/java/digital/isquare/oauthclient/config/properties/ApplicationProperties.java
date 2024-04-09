package digital.isquare.oauthclient.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import digital.isquare.oauthclient.config.properties.authentification.Auth;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class ApplicationProperties {

    private Auth auth;

    // getters and setters

}
