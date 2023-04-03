package wm.clientmvc.securities.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import wm.clientmvc.exceptions.TokenException;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
//import wm.clientmvc.securities.UserDetails.CustomerUserDetails;
//import wm.clientmvc.securities.UserDetails.EmployeeUserDetails;

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

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    //get username from jwt token
    public String getUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.get("username", String.class);
        if (username == null) {
            return "JWT claims string is empty.";
        }
        return username;
    }

    public String getIsVerified(String token){
        Claims claims =  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String is_verified = claims.get("is_verified",String.class);
        if(is_verified == null){
            return "JWT claims string is empty.";
        }
        return is_verified;
    }



    public String getUserType(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userType = claims.get("userType", String.class);
        if (userType == null) {
            return "JWT claims string is empty.";
        }
        return userType;
    }

    public String getUserID(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String userID = claims.get("userID", String.class);
        if (userID == null) {
            return "JWT claims string is empty.";
        }
        return userID;
    }

    public HashMap<Boolean,String> validateToken(String token){
        HashMap<Boolean,String> result = new HashMap<>();
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            result.put(true,"Valid");
        }catch (MalformedJwtException ex) {
            result.put(false,"Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            result.put(false,"Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            result.put(false,"Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            result.put(false,"JWT claims string is empty.");
        }
        return result;
    }



}

