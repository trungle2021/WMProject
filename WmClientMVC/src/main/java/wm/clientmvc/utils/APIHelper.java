package wm.clientmvc.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class APIHelper {
    public static <T> T makeApiCall(String url, HttpMethod method, Object payload, String token, Class<T> responseType) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
            return restTemplate.exchange(url, method, requestEntity, responseType).getBody();

    }

    public static <T> List<T> makeApiCall(String url, HttpMethod method, Object payload, String token, ParameterizedTypeReference<List<T>> responseType) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<List<T>> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
        return responseEntity.getBody();
    }

}


