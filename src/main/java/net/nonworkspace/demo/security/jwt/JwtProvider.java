package net.nonworkspace.demo.security.jwt;

import java.util.Base64;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.utils.MapUtil;

@Slf4j
@Component
public class JwtProvider {

    private SecretKey cachedSecretKey;

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) {
            cachedSecretKey = generateSecretKey();
        }
        return cachedSecretKey;
    }
    
    public String generateToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiredIn = new Date(now + 1000L * seconds);
        
        return Jwts.builder()
                .claim("body", MapUtil.json.toStr(claims))
                .setExpiration(accessTokenExpiredIn)
                .signWith(generateSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(token);
        } catch (Exception e) {
            log.error("Error verifing token: {}", ExceptionUtils.getStackTrace(e));
            return false;
        }
        
        return true;
    }
    
    public Map<String, Object> getClaims(String token) {
        String bodyJson = Jwts.parserBuilder()
                .setSigningKey(generateSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);
        
        log.debug("bodyJson: {}", bodyJson);
        
        return MapUtil.json.toMap(bodyJson);
    }
    
    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).get("userId").toString());
    }
    
    public String getUserEmail(String token) {
        return getClaims(token).get("email").toString();
    }

    private SecretKey generateSecretKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes());
    }
}
