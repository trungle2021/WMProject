package wm.clientmvc.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.DTO.LoginDTO;
import wm.clientmvc.securities.config.Interceptors.AuthenticationInterceptor;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import static wm.clientmvc.utils.SD_CLIENT.api_customerLoginUrl;
import static wm.clientmvc.utils.SD_CLIENT.api_refresh_token;


public class APIHelper {

    @Value("${app-jwt-expiration-milisecond}")
    private  static long jwtExpirationDate;

    @Value("${app-jwt-refresh-milisecond}")
    private long jwtRefresh;


//    public static <T> T makeApiCall(String url, HttpMethod method, Object payload, String token, Class<T> responseType) throws IOException {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        if (StringUtils.hasLength(token)) {
//            headers.setBearerAuth(token);
//        }
//           HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
//           return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
//    }

    public static <T> T makeApiCall(String url, HttpMethod method, Object payload, String token, Class<T> responseType,HttpServletRequest request, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<T> responseEntity = null;
        int retry = 0;
        int retryCount = 2;
        do {
            try {
                responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
            } catch (HttpClientErrorException ex) {
                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    String newToken = APIHelper.refreshTokenAndGetNewToken(request,response,ex); // implement your refresh token logic here
                    headers.setBearerAuth(newToken);
                    requestEntity = new HttpEntity<>(payload, headers);
                } else {
                    throw ex;
                }
            }
            retry++;
        } while (responseEntity == null && retry < retryCount);

        if (responseEntity != null) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Unable to retrieve response from API after " + retryCount + " retries");
        }
    }

    private static String refreshTokenAndGetNewToken(HttpServletRequest request,HttpServletResponse response,HttpClientErrorException ex) throws IOException {
        String refreshToken = null;
        String newAccessToken = null;
        refreshToken = APIHelper.getCookie(request, "refresh_token");

        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {

            JWTAuthResponse  jwtAuthResponse = APIHelper.makeApiCall(api_refresh_token + refreshToken, HttpMethod.POST,null , null, JWTAuthResponse.class, request,response);
            if (jwtAuthResponse != null) {
                newAccessToken = jwtAuthResponse.getAccessToken();
                if (StringUtils.hasLength(newAccessToken)) {
                    response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + newAccessToken);

                    // Set the new access token in the cookie
                    Cookie accessTokenCookie = new Cookie("token", newAccessToken);
                    accessTokenCookie.setMaxAge((int) jwtExpirationDate); // set the cookie expiration time to 1 hour
                    accessTokenCookie.setHttpOnly(true);
                    accessTokenCookie.setSecure(true); // set the cookie to be sent only over HTTPS
                    response.addCookie(accessTokenCookie);
                }
            }
        }
        return newAccessToken;
    }

    public static <T> List<T> makeApiCall(String url, HttpMethod method, Object payload, String token, ParameterizedTypeReference<List<T>> responseType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<List<T>> responseEntity = null;
        int retry = 0;
        int retryCount = 2;
        do {
            try {
                responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
            } catch (HttpClientErrorException ex) {
                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    String newToken = APIHelper.refreshTokenAndGetNewToken(request,response,ex); // implement your refresh token logic here
                    headers.setBearerAuth(newToken);
                    requestEntity = new HttpEntity<>(payload, headers);
                } else {
                    throw ex;
                }
            }
            retry++;
        } while (responseEntity == null && retry < retryCount);

        if (responseEntity != null) {
            return responseEntity.getBody();
        } else {
            throw new RuntimeException("Unable to retrieve response from API after " + retryCount + " retries");
        }
    }

    public static String getCookie(HttpServletRequest request,String cookieName){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}


