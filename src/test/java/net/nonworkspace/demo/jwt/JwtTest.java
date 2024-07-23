package net.nonworkspace.demo.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.security.jwt.JwtProvider;

@SpringBootTest
@Transactional
@Slf4j
public class JwtTest {

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    @Autowired
    private JwtProvider jwtProvider;

    @Test
    void makeSecretKey() {
        // given
        String encodedKey = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());

        // when
        SecretKey secretKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
        log.debug("encodedKey: {}", secretKey.getEncoded());

        // then
        assertThat(secretKey).isNotNull();
    }

    @Test
    void getAccessTokenTest() {
        // given
        Claims claims = Jwts.claims();
        claims.put("userId", 240);
        claims.put("username", "user1");

        // when
        String accessToken = jwtProvider.generateToken(claims, 60 * 60 * 1); // valid in 1 hour
        log.debug("accessToken: {}", accessToken);

        // then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("accessToken 을 통해서 claims 를 얻을 수 있다.")
    void getClaimsTest() {
        // given
        Map<String, Object> claims = Jwts.claims();
        claims.put("userId", 240L);
        claims.put("username", "user1");

        // when
        String accessToken = jwtProvider.generateToken(claims, 60 * 60 * 1); // valid in 1 hour

        // then
        assertThat(jwtProvider.verify(accessToken)).isTrue();

        Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
        log.debug("claimsFromToken userId: {}", Long.parseLong(claimsFromToken.get("userId").toString()));
        log.debug("claimsFromToken username: {}", claimsFromToken.get("username").toString());

        assertThat(Long.parseLong(claims.get("userId").toString()) == Long.parseLong(claimsFromToken.get("userId").toString()));
        assertThat(claims.get("username").toString()
                .equals(claimsFromToken.get("username").toString()));
    }

    // @Test
    @DisplayName("만료된 토큰을 유효하지 않다.")
    void getClaimWhenTokenExpired() throws InterruptedException {
        // given
        Map<String, Object> claims = Jwts.claims();
        claims.put("userId", 240);
        claims.put("username", "user1");

        // when
        String accessToken = jwtProvider.generateToken(claims, 10 * 1 * 1); // valid in 10 sec
        assertThat(jwtProvider.verify(accessToken)).isTrue();

        Thread.sleep(1000 * 10 * 1);

        // then
        log.debug("=== after 10 seconds ====");
        assertThat(jwtProvider.verify(accessToken)).isFalse();
    }
}
