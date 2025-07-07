package com.coveragex.todoapplication.utility;

import com.coveragex.todoapplication.exception.CustomException;
import com.coveragex.todoapplication.utility.errorcode.JwtErrorCodes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.accessTokenexpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    public Claims getClaimsFromToken(String token) {
        try{
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();}
        catch (Exception e){
            throw new CustomException(JwtErrorCodes.JWT_TOKEN_DECODE_ERROR_CODE, "Jwt issue");
        }
    }

    public String generateToken(Map<String, Object> claims, String subject, long expiration) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        } catch (Exception e) {
            throw new CustomException(JwtErrorCodes.JWT_TOKEN_GENERATE_ERROR_CODE, "JWT issue");
        }
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String generateAccessToken(Map<String, Object> claims, String subject) {
        return generateToken(claims, subject, accessTokenExpiration);
    }

    public String generateRefreshToken(Map<String, Object> claims, String subject) {
        return generateToken(claims, subject, refreshTokenExpiration);
    }

}
