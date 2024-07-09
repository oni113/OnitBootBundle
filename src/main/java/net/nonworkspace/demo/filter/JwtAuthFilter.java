package net.nonworkspace.demo.filter;

import java.io.IOException;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.security.jwt.JwtProvider;
import net.nonworkspace.demo.service.DemoUserDetailService;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private final DemoUserDetailService demoUserDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        
        log.debug("== header Authorization: {} ===", header);
        
        try {
            if (header != null && header.startsWith("Bearer ")) {
                String tokenInHeader = header.substring(7);

                if (!jwtProvider.verify(tokenInHeader)) {
                    filterChain.doFilter(request, response);
                    return;
                }

                Map<String, Object> claimsFromToken = jwtProvider.getClaims(tokenInHeader);
                Long userId = Long.parseLong(claimsFromToken.get("userId").toString());
                
                log.debug("userId from claimsFromToken: {}", userId);
                
                UserDetails userDetails = demoUserDetailService.loadUserByUserId(userId);

                if (userDetails == null) {
                    filterChain.doFilter(request, response);
                    return;
                }

                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

}
