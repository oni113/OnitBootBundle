package net.nonworkspace.demo.service;

import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.domain.dto.LoginRequestDto;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.security.jwt.JwtProvider;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
@Slf4j
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final DemoUserDetailService demoUserDetailService;

    public String getLoginToken(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();

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

        String accessToken = jwtProvider.generateToken(claims, (60 * 60 * 1));
        // String refreshToken = jwtProvider.generateToken(MapUtil.json.toMap(userInfo.toString()),
        // (60 * 60 * 1));

        return accessToken;
    }

    public DemoUserDetails getUserDetailFromToken(String accessToken) {

        return null;
    }
}
