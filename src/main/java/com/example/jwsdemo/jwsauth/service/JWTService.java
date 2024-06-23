package com.example.jwsdemo.jwsauth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTService {

    private static final String SECRET = "357638792F423F4428472B4B6250655368566D597133743677397A2443264629";
    private final long EXPIRATIONTIME = 1000l*60*1;

    public String extractUserName(String token){
    return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
    return extractClaim(token,Claims::getExpiration);
    }
    public <T> T extractClaim(String token , Function<Claims , T> claimsResolver){
        Claims claim = extractAllClaims(token);
        return claimsResolver.apply(claim);
    }

    public boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token , UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String userName){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims , userName);
    }

    public String createToken(Map<String, Object> claims , String username){
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATIONTIME))
                .signWith(getSignKey() , SignatureAlgorithm.HS256)
                .compact();

    }

    private Key getSignKey() {
        byte [] data = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(data);
    }

}
