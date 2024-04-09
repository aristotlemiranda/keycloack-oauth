package digital.isquare.oauthclient.config.properties;

import digital.isquare.oauthclient.config.properties.provider.Provider;
import digital.isquare.oauthclient.config.properties.registration.Registration;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
@Getter
@Setter
public class KeycloakProperties {

    private Registration registration;
    private Provider provider;

    // getters and setters

}
