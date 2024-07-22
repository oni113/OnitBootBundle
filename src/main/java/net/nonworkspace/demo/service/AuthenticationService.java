package net.nonworkspace.demo.service;

import io.jsonwebtoken.Jwts;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.dto.LoginRequestDto;
import net.nonworkspace.demo.domain.dto.UserInfoDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final DemoUserDetailService demoUserDetailService;

    @Value("${jwt.expiration_time}")
    private int expireTime;

    public String getLoginToken(LoginRequestDto dto) {
        String email = dto.email();
        String password = dto.password();

        DemoUserDetails userDetails =
            (DemoUserDetails) demoUserDetailService.loadUserByUsername(email);
        String recentPassword = userDetails.getUserInfoDto().getPassword();
        if (recentPassword == null || recentPassword.isEmpty()) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_EXPIRED);
        }

        boolean passwordMatched = passwordEncoder.matches(password, recentPassword);

        if (!passwordMatched) {
            throw new CommonBizException(CommonBizExceptionCode.PASSWORD_NOT_MATCHED);
        }

        Map<String, Object> claims = Jwts.claims();
        claims.put("userId", userDetails.getUserInfoDto().getUserId());
        claims.put("email", userDetails.getUserInfoDto().getEmail());
        claims.put("name", userDetails.getUserInfoDto().getName());

        String accessToken = jwtProvider.generateToken(claims, expireTime);

        return accessToken;
    }

    public UserInfoDto getUserDetailFromToken(String accessToken) {
        Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
        Long userId = Long.parseLong(claimsFromToken.get("userId").toString());

        DemoUserDetails userDetails = (DemoUserDetails) Optional.ofNullable(
                demoUserDetailService.loadUserByUserId(userId))
            .orElseThrow(() -> new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER));
        UserInfoDto result = userDetails.getUserInfoDto();
        result.setPassword(null);
        return result;
    }
}
