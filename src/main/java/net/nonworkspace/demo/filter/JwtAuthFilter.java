package net.nonworkspace.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.DemoUserDetails;
import net.nonworkspace.demo.exception.common.CommonBizException;
import net.nonworkspace.demo.exception.common.CommonBizExceptionCode;
import net.nonworkspace.demo.security.jwt.JwtProvider;
import net.nonworkspace.demo.service.DemoUserDetailService;
import net.nonworkspace.demo.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final DemoUserDetailService demoUserDetailService;

    @Value("${custom.jwt.cookie}")
    private final String tokenCookieName;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (!(requestURI.startsWith("/user/") || requestURI.startsWith("/admin/")
            || requestURI.startsWith("/batch/") || requestURI.startsWith("/api/auth/signout"))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 쿠키에서 토큰 읽음
        String serverToken = CookieUtil.getCookieValue(request, tokenCookieName);
        log.debug("== server token: {}", serverToken);

        if (!jwtProvider.verify(serverToken)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        }

        // 2. 헤더에서 토큰 읽음
        String header = request.getHeader("Authorization");
        log.debug("== header Authorization: {} ===", header);

        String clientToken = "";

        if (header == null || header.isEmpty()) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        }

        if (header.startsWith("Bearer ")) {
            clientToken = header.substring(7);
            log.debug("== client token: {}", clientToken);
        }

        if (!jwtProvider.verify(clientToken)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        }

        if (!serverToken.equals(clientToken)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        }

        Map<String, Object> claimsFromToken = jwtProvider.getClaims(serverToken);
        Long userId = Long.parseLong(claimsFromToken.get("userId").toString());

        log.debug("userId from claimsFromToken: {}", userId);

        DemoUserDetails userDetails = demoUserDetailService.loadUserByUserId(userId);

        if (userDetails == null) {
            throw new CommonBizException(CommonBizExceptionCode.NOT_EXIST_MEMBER);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
