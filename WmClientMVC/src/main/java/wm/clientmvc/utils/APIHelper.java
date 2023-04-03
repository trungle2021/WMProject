package wm.clientmvc.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.JWTAuthResponse;
import wm.clientmvc.exceptions.TokenException;
import wm.clientmvc.exceptions.UnauthorizedException;
import wm.clientmvc.securities.JWT.JwtTokenProvider;
import java.io.IOException;
import java.util.*;
import static wm.clientmvc.utils.SD_CLIENT.api_refresh_token;

@Component
public class APIHelper {

    @Value("${app-jwt-expiration-milisecond}")
    private static long jwtExpirationDate;

    @Value("${app-jwt-refresh-milisecond}")
    private long jwtRefresh;

    private static JwtTokenProvider tokenProvider;

    @Autowired
    public APIHelper(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    public static <T> T makeApiCall(String url, HttpMethod method, Object payload, String token, Class<T> responseType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {

            validateToken(token,headers,request,response);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
        return responseEntity.getBody();


    }

    private static String refreshTokenAndGetNewToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = null;
        String newAccessToken = null;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

        ResponseEntity<JWTAuthResponse> responseEntity = null;
        refreshToken = APIHelper.getCookie(request, "refresh_token");

        try{
            responseEntity = restTemplate.exchange(api_refresh_token + refreshToken, HttpMethod.POST, requestEntity, JWTAuthResponse.class);
        }catch (HttpClientErrorException ex){
            throw new UnauthorizedException("Session Expired!");
        }
        JWTAuthResponse jwtAuthResponse = responseEntity.getBody();
        System.out.println("Call API Refresh Token");
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
        return newAccessToken;
    }

    public static <T> List<T> makeApiCall(String url, HttpMethod method, Object payload, String token, ParameterizedTypeReference<List<T>> responseType, HttpServletRequest request, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (StringUtils.hasLength(token)) {
            validateToken(token,headers,request,response);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<List<T>> responseEntity = null;
        responseEntity = restTemplate.exchange(url, method, requestEntity, responseType);
        return responseEntity.getBody();

    }

    public static String getCookie(HttpServletRequest request, String cookieName) {
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

    public static void validateToken(String token, HttpHeaders headers, HttpServletRequest request, HttpServletResponse response) throws IOException {

        HashMap<Boolean,String> checkTokenResult = tokenProvider.validateToken(token);

        if(checkTokenResult.containsKey(false)){
            boolean isExpired = checkTokenResult.get(false).toString().equals("Expired JWT token");
            if(isExpired){
                String newAccessToken = APIHelper.refreshTokenAndGetNewToken(request,response);
                headers.setBearerAuth(newAccessToken);
            }else{
                throw new TokenException(checkTokenResult.get("false"));
            }
        }else{
            headers.setBearerAuth(token);
        }
    }

}


