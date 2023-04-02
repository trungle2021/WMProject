package com.springboot.wmproject.securities.JWT;

import com.springboot.wmproject.DTO.CustomerAccountDTO;
import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.DTO.RefreshTokenDTO;
import com.springboot.wmproject.entities.RefreshToken;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.RefreshTokenRepository;
import com.springboot.wmproject.securities.UserDetails.CustomUserDetails;
import com.springboot.wmproject.services.AuthServices.RefreshTokenService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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



    //generate JWT token
    public String generateAccessToken(Authentication authentication){
        Date expirationDate;
        String username = authentication.getName();
        String userType = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        String userID = "";
        String is_verified = "";
        Object userDetail = authentication.getPrincipal();
        if(userDetail instanceof CustomUserDetails) {
            userID =    ((CustomUserDetails) userDetail).getUserId().toString();
            is_verified =    String.valueOf(((CustomUserDetails) userDetail).is_verified());
            Map<String, String> claims = new HashMap<>();
            claims.put("userID",userID);
            claims.put("username",username);
            claims.put("userType",userType);
            claims.put("is_verified",is_verified);


            Date currentDate = new Date();


            expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);


            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(key())
                    .compact();
            return token;

        }else{
            throw new WmAPIException(HttpStatus.BAD_REQUEST,"Cannot generate token");
        }

    }

    public String generateRefreshToken(Authentication authentication,Object user){
        Date expirationDate;
        String username = authentication.getName();
        String userType = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        String userID = "";
        String is_verified = "";
        Object userDetail = authentication.getPrincipal();
        if(userDetail instanceof CustomUserDetails) {
            userID =    ((CustomUserDetails) userDetail).getUserId().toString();
            is_verified =    String.valueOf(((CustomUserDetails) userDetail).is_verified());
            Map<String, String> claims = new HashMap<>();
            claims.put("userID",userID);
            claims.put("username",username);
            claims.put("userType",userType);
            claims.put("is_verified",is_verified);

            Date currentDate = new Date();
            expirationDate = new Date(currentDate.getTime() + jwtRefresh);

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .signWith(key())
                    .compact();

            RefreshToken refreshToken = new RefreshToken();

            if(user instanceof CustomerAccountDTO){
                refreshToken.setCustomerId(((CustomerAccountDTO) user).getId());
            }else if(user instanceof EmployeeAccountDTO){
                refreshToken.setEmployeeId(((EmployeeAccountDTO) user).getId());
            }else{
                return "Account ID is required";
            }

            refreshToken.setToken(token);
            refreshToken.setExpiryDate(expirationDate.toString());
            RefreshTokenDTO tokenDTO = refreshTokenService.create(refreshToken);
            return tokenDTO.getToken();

        }else{
            throw new WmAPIException(HttpStatus.BAD_REQUEST,"Cannot generate token");
        }
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

