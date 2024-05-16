package digital.isquare.oauthclient.controller;

import digital.isquare.oauthclient.config.properties.ApplicationProperties;
import digital.isquare.oauthclient.config.properties.KeycloakProperties;
import digital.isquare.oauthclient.service.CustomRestTemplate;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomRestTemplate customRestTemplate;

    @Autowired
    private KeycloakProperties keycloakProperties;
    @Autowired
    private ApplicationProperties applicationProperties;

    private String accessToken;

    @BeforeEach
    void setUp() {

        String url = "http://localhost:8081/customers";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", "springboot-openid-client-app");
        params.add("client_secret", "0IezYl1fYSX3XuyW4ZlayA9KqisItfQ0");
        params.add("username", "my-user");
        params.add("password", "P@ssw0rd");
        params.add("grant_type", "password");
        params.add("scope", "openid");

        UriComponentsBuilder builder;
        builder = UriComponentsBuilder.fromHttpUrl(url);
        Map<String, String> response = restTemplate.postForObject(builder.toUriString(), null, Map.class);

        assert response != null;
        accessToken = response.get("access_token");
    }
    @Test
    void getPublic() throws Exception {
        mockMvc.perform(get("/api/public")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenu Public"));
    }

    @Test
    void getPrivate() throws Exception {
        //
        getAccessToken();

        //
        mockMvc.perform(get("/api/private")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Contenu Privé"));
    }

    private void getAccessToken() {
        String urlParam = "/protocol/openid-connect/token";
        String url = keycloakProperties.getProvider().getKeycloak().getIssuerUri().concat(urlParam);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", keycloakProperties.getRegistration().getKeycloak().getClientId());
        params.add("client_secret", keycloakProperties.getRegistration().getKeycloak().getClientSecret());
        params.add("username", applicationProperties.getAuth().getUsername());
        params.add("password", applicationProperties.getAuth().getPassword());
        params.add("grant_type", applicationProperties.getAuth().getGrantType());
        params.add("scope", keycloakProperties.getRegistration().getKeycloak().getScope());

        ResponseEntity<Map> responseEntity = customRestTemplate.post(url, params, Map.class);
        Map<String, String> response = responseEntity.getBody();

        assert response != null;
        accessToken = response.get("access_token");
    }
}