package net.nonworkspace.demo.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.filter.JwtAuthFilter;
import net.nonworkspace.demo.security.jwt.JwtProvider;
import net.nonworkspace.demo.service.DemoUserDetailService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final DemoUserDetailService demoUserDetailService;

    private final JwtProvider jwtProvider;

    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin((form) -> form.disable()).httpBasic(AbstractHttpConfigurer::disable)
                .csrf((csrfConfig) -> {
                    csrfConfig.disable();
                }).headers((headerConfig) -> {
                    headerConfig.frameOptions(frameOptions -> {
                        frameOptions.sameOrigin();
                    }).addHeaderWriter(
                            new StaticHeadersWriter("X-XSS-Protection", "1; mode=block"));
                }).cors((cors) -> {
                    CorsConfigurationSource source = ((request) -> {
                        CorsConfiguration config = new CorsConfiguration();

                        // add allowed origins
                        List<String> allowedOrigins = List.of("*");
                        config.setAllowedOrigins(allowedOrigins);

                        // add allowed http method
                        List<String> allowedMethods =
                                List.of("GET", "POST", "PUT", "DELETE", "PATCH");
                        config.setAllowedMethods(allowedMethods);

                        return config;
                    });
                    cors.configurationSource(source);
                }).authorizeHttpRequests((authorizeRequests) -> {
                    authorizeRequests.requestMatchers(HttpMethod.GET, "/").permitAll()
                            .requestMatchers("/", "/login").permitAll()
                            .requestMatchers("/", "/login/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/v*/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/swagger-ui/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/error").permitAll()
                            .requestMatchers("/", "/admin/**").hasRole("ADMIN")
                            .requestMatchers("/", "/user/**").hasRole("USER")
                            .requestMatchers("/", "/api/**").permitAll().anyRequest()
                            .authenticated();
                })
                .sessionManagement((sessionManagement) -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))    // never create session
                .addFilterBefore(new JwtAuthFilter(jwtProvider, demoUserDetailService),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
