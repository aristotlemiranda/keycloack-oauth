package digital.isquare.oauthclient.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

   /* @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.sendRedirect("/");
    }*/

   /* @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        // Check if the authentication object contains user details
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();

            // Check if the user has the required role (e.g., "ROLE_USER")
            if (authorities.contains("ROLE_USER")) {
                // User has the required role
                // Perform further actions or redirect accordingly
                super.onAuthenticationSuccess(request, response, authentication);
            } else {
                // Handle unauthorized access (e.g., redirect to access denied page)
                response.sendRedirect("/access-denied");
            }
        } else {
            // Handle invalid authentication scenario
            response.sendRedirect("/login-error");
        }
    }*/


    //FOR OIDC_USER
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        // Check if the authentication object contains user details
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();

            // Check if the user has the required role (e.g., "OIDC_USER")
            if (hasRole(oauth2User, "OIDC_USER")) {
                // User has the required role
                // Perform further actions or redirect accordingly
                super.onAuthenticationSuccess(request, response, authentication);
            } else {
                // Handle unauthorized access (e.g., redirect to access denied page)
                response.sendRedirect("/access-denied");
            }
        } else {
            // Handle invalid authentication scenario
            response.sendRedirect("/login-error");
        }
    }

    private boolean hasRole(OAuth2User oauth2User, String roleName) {
        Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();
        return authorities.contains(roleName);
    }

}
