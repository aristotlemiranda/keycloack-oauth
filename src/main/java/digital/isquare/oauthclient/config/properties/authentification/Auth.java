package digital.isquare.oauthclient.config.properties.authentification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {

    private String username;
    private String password;
    private String grantType;
}
