package com.springboot.wmproject.security.JWT;

import com.springboot.wmproject.exceptions.WmAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
    @Value("${app.jwt-secrect}")
    private String jwtSecrect;
    @Value("${app-jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    //generate JWT token
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        String userType = authentication.getAuthorities().stream().findFirst().get().getAuthority();
        Map<String, String> claims = new HashMap<>();
        claims.put("userType",userType);
        claims.put("username",username);

        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpirationDate);

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
        return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecrect));
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

