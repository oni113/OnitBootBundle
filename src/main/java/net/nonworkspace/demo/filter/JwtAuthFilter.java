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
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final DemoUserDetailService demoUserDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (!(requestURI.startsWith("/user/") || requestURI.startsWith("/admin/")
            || requestURI.startsWith("/batch/"))) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 쿠키에서 토큰 읽음
        String token;
        try {
            token = CookieUtil.getCookieValue(request, "auth_req");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        log.debug("== cookie token: {}", token);

        // 2. 헤더에서 토큰 읽음
        String header = request.getHeader("Authorization");
        log.debug("== header Authorization: {} ===", header);
        if (token.isEmpty() && header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        if (!jwtProvider.verify(token)) {
            throw new CommonBizException(CommonBizExceptionCode.ACCESS_NOT_ALLOWED);
        }

        Map<String, Object> claimsFromToken = jwtProvider.getClaims(token);
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
