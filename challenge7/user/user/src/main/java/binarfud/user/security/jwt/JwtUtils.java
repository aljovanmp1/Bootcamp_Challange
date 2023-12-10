package binarfud.user.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;

import binarfud.user.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        Date now = new Date();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(String username, String otp) throws JacksonException{
        OtpJwtDto data = new OtpJwtDto();
        data.setOtp(otp);
        data.setUsername(username);
        
        Map<String, OtpJwtDto> jwt = new HashMap();
        jwt.put("data", data);

        ObjectMapper objectMapper = new ObjectMapper();

        String jacksonData = "";
        jacksonData = objectMapper.writeValueAsString(jwt);

        Date now = new Date();
        return Jwts.builder()
                .setSubject(jacksonData)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtSecret));
    }
    public String getUsername(String jwt) {
        String username =  Jwts.parserBuilder()
                .setSigningKey(key()).build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
        return username;
    }
}
