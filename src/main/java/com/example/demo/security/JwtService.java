package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct; // <-- ADD THIS IMPORT
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // =================================================================
    // === ADD THIS METHOD TO PRINT THE KEY ON STARTUP =================
    // =================================================================
    @PostConstruct
    public void init() {
        System.out.println("====================================================================");
        System.out.println("!!! JWT SECRET KEY IN USE: " + secretKey);
        System.out.println("====================================================================");
    }
    // =================================================================

    /**
     * Trích xuất username (subject) từ token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Trích xuất một claim cụ thể từ token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Sinh token chỉ với UserDetails.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Sinh token với các claims bổ sung.
     */
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .setClaims(extraClaims) // Thêm các claims bổ sung
                .setSubject(userDetails.getUsername()) // Set subject là username
                .setIssuedAt(new Date(System.currentTimeMillis())) // Thời gian phát hành
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Thời gian hết hạn
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token
                .compact();
    }

    /**
     * Kiểm tra xem token có hợp lệ không.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Token hợp lệ khi username khớp và token chưa hết hạn
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }



    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Trích xuất toàn bộ claims từ token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Lấy key dùng để ký token.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}