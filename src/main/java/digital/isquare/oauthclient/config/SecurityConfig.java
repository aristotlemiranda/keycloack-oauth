package digital.isquare.oauthclient.config;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
class SecurityConfig {

    private static final String GROUPS = "groups";
    private static final String REALM_ACCESS_CLAIM = "realm_accessy";
    private static final String ROLES_CLAIM = "roles";

    private final KeycloakLogoutHandler keycloakLogoutHandler;

    SecurityConfig(KeycloakLogoutHandler keycloakLogoutHandler) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    @Bean
    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // Allows preflight requests from browser
                .requestMatchers(new AntPathRequestMatcher("/api/public"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/about"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/contact"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/service"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/privacy"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/static/images/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/static/css/**"))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/customers/**", HttpMethod.OPTIONS.name()))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher("/customers/**"))
                .hasRole("user")
                .anyRequest()
                .authenticated());
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));
        http.oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2LoginSuccessHandler())) // Utiliser le gestionnaire de succès personnalisé
                .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
        return http.build();
    }


    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            var authority = authorities.iterator().next();
            boolean isOidc = authority instanceof OidcUserAuthority;

            if (isOidc) {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                // Tokens can be configured to return roles under
                // Groups or REALM ACCESS hence have to check both
                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                } else if (userInfo.hasClaim(GROUPS)) {
                    Collection<String> roles = userInfo.getClaim(GROUPS);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            } else {
                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(REALM_ACCESS_CLAIM);
                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
                }
            }
            return mappedAuthorities;
        };
    }

    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
                Collectors.toList());
    }
}
