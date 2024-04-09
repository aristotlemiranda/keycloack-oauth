package digital.isquare.oauthclient.config.properties.registration;

import lombok.*;
@Getter
@Setter
public class Keycloak {
    private String clientId;
    private String clientSecret;
    private String authorizationGrantType;
    private String scope;

    // getters and setters
}