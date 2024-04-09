package digital.isquare.oauthclient.config;

import digital.isquare.oauthclient.config.listener.SessionListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfig {

    @Bean
    public ServletListenerRegistrationBean<SessionListener> sessionListenerWithLogout() {
        ServletListenerRegistrationBean<SessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
        listenerRegBean.setListener(new SessionListener());
        return listenerRegBean;
    }
}
