package digital.isquare.oauthclient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomRestTemplate {

    private final RestTemplate restTemplate;

    public CustomRestTemplate(@Autowired RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> get(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(params), responseType);
    }

    public <T> ResponseEntity<T> post(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        return restTemplate.postForEntity(url, request, responseType);    }

    public <T> ResponseEntity<T> put(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(params), responseType);
    }

    public <T> ResponseEntity<T> delete(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        return restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>(params), responseType);
    }
}
