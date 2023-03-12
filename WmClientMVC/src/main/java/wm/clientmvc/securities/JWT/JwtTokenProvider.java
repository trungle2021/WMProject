package wm.clientmvc.securities.JWT;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
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
    @Value("${app-jwt-expiration-second}")
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


}

