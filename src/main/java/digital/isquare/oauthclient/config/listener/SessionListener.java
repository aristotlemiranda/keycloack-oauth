package digital.isquare.oauthclient.config.listener;

import digital.isquare.oauthclient.config.KeycloakLogoutHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration
public class SessionListener implements HttpSessionListener {

    private static final String SESSION_ID = "sessionId";
    private static final String AUTHENTICATION = "authentication";

    @Autowired
    private KeycloakLogoutHandler keycloakLogoutHandler;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String sessionId = se.getSession().getId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        se.getSession().setAttribute(SESSION_ID, sessionId);
        if (authentication != null) {
            se.getSession().setAttribute(AUTHENTICATION, authentication);
        }
        log.info("Session created with ID: {}", sessionId);
        MDC.put(SESSION_ID, sessionId);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Retrieve the session ID and Authentication object from the session attributes
        String sessionId = (String) se.getSession().getAttribute(SESSION_ID);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Session destroyed with ID: {}", sessionId);
        MDC.put(SESSION_ID, sessionId);


        log.info("Session destroyed with authentication: {}", authentication);
        // Log out the user
        if (authentication != null) {
            logOut(authentication);
        }

        // Remove the session ID from MDC
        MDC.remove(SESSION_ID);
    }

    private void logOut(Authentication authentication) {
        log.info("Logging out user: {}", authentication.getName());
        keycloakLogoutHandler.logout(null, authentication);
    }
}
