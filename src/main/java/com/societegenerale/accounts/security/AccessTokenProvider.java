package com.societegenerale.accounts.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.societegenerale.accounts.dtos.TokenInfo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AccessTokenProvider {


    @Value("${security.tokenExpiryInMs}")
    private int expiryTime;

    @Value("${security.secretKey}")
    private String secretKey;

    public TokenInfo createToken(Authentication authentication) {

        CustomUserDetail userPrincipal = (CustomUserDetail) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiryTime);
        String token = Jwts.builder().setSubject(Long.toString(userPrincipal.getId())).setIssuedAt(new Date()).setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return TokenInfo.builder().token(token).type("bearer").user(userPrincipal.getName()).build();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
        	log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
        	log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
        	log.error("JWT claims string is empty.");
        }
        return false;
    }
}
