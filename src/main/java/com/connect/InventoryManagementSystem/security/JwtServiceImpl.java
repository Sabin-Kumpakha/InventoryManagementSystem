package com.connect.InventoryManagementSystem.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET = "B9F1BA40840E19A5387C1F370AB4A2C80480CA5290FBB3B826F8104546C3104DDBFFCFCC197904CEE975705D8893735F2DA1CB4E984E475A355FC1F9AD6FE40A";

    private static final long VALIDITY = TimeUnit.MINUTES.toMillis(30);

    @Override
    public String generateToken(UserDetails userDetails){
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", "http://www.jwt.io");
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey(){
        byte[] decodedKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodedKey);          // converts decodedKey into secretKey
    }

    @Override
    public String extractUsername(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }

    private Claims getClaims(String jwt){
        return Jwts.parser()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
