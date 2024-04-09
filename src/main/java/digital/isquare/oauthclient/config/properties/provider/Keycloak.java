package digital.isquare.oauthclient.config.properties.provider;

import lombok.*;

@Getter
@Setter
public class Keycloak {
    private String issuerUri;
    private String userNameAttribute;

    // getters and setters
}
