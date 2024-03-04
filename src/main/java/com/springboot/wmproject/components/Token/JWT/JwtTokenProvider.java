package com.springboot.wmproject.components.Token.JWT;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.RefreshToken;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
import com.springboot.wmproject.components.Token.RefreshToken.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.springboot.wmproject.utils.SD.*;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecret;
    @Value("${app-jwt-expiration-milisecond}")
    private long jwtExpirationDate;

    @Value("${app-jwt-refresh-milisecond}")
    private long jwtRefresh;

    private RefreshTokenService refreshTokenService;

    @Autowired
    public JwtTokenProvider(RefreshTokenService refreshTokenService) {
        this.refreshTokenService = refreshTokenService;
    }

    private Object extractPrincipalFromAuthenticationObject(Authentication authentication){
        return authentication.getPrincipal();
    }
    private Map<String, String> createClaimsFromAuthenticationObject(Authentication authentication){
        String userID;
        String isVerified ;
        String username = authentication.getName();

        Optional<String> userTypeOptional = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst();

        String userType = userTypeOptional.orElse(null);
        Object principalFromAuthenticationObject = extractPrincipalFromAuthenticationObject(authentication);
        if(principalFromAuthenticationObject instanceof CustomUserDetails userDetails) {
            userID = String.valueOf(userDetails.getUserId());
            isVerified = String.valueOf(userDetails.is_verified());

            Map<String, String> claims = new HashMap<>();
            claims.put(USER_ID,userID);
            claims.put(USERNAME,username);
            claims.put(USER_TYPE,userType);
            claims.put(IS_VERIFIED,isVerified);
            return claims;
        }else{
            throw new WmAPIException(HttpStatus.BAD_REQUEST,"Cannot generate token");
        }
    }

    private Date getExpirationDate(Long jwtExpirationDate){
        Date currentDate = new Date();
        return new Date(currentDate.getTime() + jwtExpirationDate);
    }
    private String generateTokenFromAuthenticationObject(Authentication authentication, Date expirationDate){
        Map<String, String> claims = createClaimsFromAuthenticationObject(authentication);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    //generate JWT token
    public String generateAccessToken(Authentication authentication){
           Date expirationDate = getExpirationDate(jwtExpirationDate);
           return generateTokenFromAuthenticationObject(authentication, expirationDate);
    }

    public String generateRefreshToken(Authentication authentication,Object user) {
        Date expirationDate = getExpirationDate(jwtRefresh);
        String token = generateTokenFromAuthenticationObject(authentication, expirationDate);
        RefreshToken refreshToken = new RefreshToken();

        if (user instanceof CustomerAccountDTO customerAccountDTO) {
            refreshToken.setCustomerId(customerAccountDTO.getId());
        } else if (user instanceof EmployeeAccountDTO employeeAccountDTO) {
            refreshToken.setEmployeeId(employeeAccountDTO.getId());
        } else {
            return "Account ID is required";
        }

        refreshToken.setToken(token);
        refreshToken.setExpiryDate(expirationDate.toString());
        return refreshTokenService.create(refreshToken).getToken();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get username from jwt token
    public String getUsername(String token){
      Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.get("username",String.class);
        if(username == null){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
        return username;
    }
    public String getUserType(String token){
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userType = claims.get("userType",String.class);
        if(userType == null){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
        return userType;
    }

    public String getUserID(String token){
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userID = claims.get("userID",String.class);
        if(userID == null){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
        return userID;
    }

    public String getIsVerified(String token){
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String is_verified = claims.get("is_verified",String.class);
        if(is_verified == null){
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
        return is_verified;
    }

    //validate Jwt Token
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        }catch (MalformedJwtException ex) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }
}

