package wm.clientmvc.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletResponse;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class APIHelper {


    public static <T> T makeApiCall(String url, HttpMethod method, Object payload, String token, Class<T> responseType) throws IOException {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String currentPath = request.getServletPath();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
       try{
           HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
           return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
       }catch (HttpClientErrorException e){
           String status = String.valueOf(e.getStatusCode().value());
           if(status.equals("401")){
               if(currentPath.startsWith("/staff")){
                   response.sendRedirect("/staff/login");
               }else if(currentPath.startsWith("/customers")) {
                   response.sendRedirect("/login");
               }
           }else if(status.equals("403")){
               response.sendRedirect("/access-denied");
           }else{
               throw e;
           }

       }
        return null;
    }

    public static <T> List<T> makeApiCall(String url, HttpMethod method, Object payload, String token, ParameterizedTypeReference<List<T>> responseType) throws IOException {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                        .getRequest();

        String currentPath = request.getServletPath();

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getResponse();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        try{
            ResponseEntity<List<T>> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
            return responseEntity.getBody();
        }catch (HttpClientErrorException e){
            String status = String.valueOf(e.getStatusCode().value());
            if(status.equals("401")){
                if(currentPath.startsWith("/staff")){
                    response.sendRedirect("/staff/login");
                }else if(currentPath.startsWith("/customers")) {
                    response.sendRedirect("/login");
                }
            }else if(status.equals("403")){
                response.sendRedirect("/access-denied");
            }else{
                throw e;
            }
        }
        return null;
    }

}


