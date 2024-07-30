package net.nonworkspace.demo.domain;

import java.util.Collection;
import java.util.HashSet;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.user.UserInfoDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@Slf4j
public record DemoUserDetails(UserInfoDto userInfoDto) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        HashSet<GrantedAuthority> authorities = new HashSet<>();
        userInfoDto.roles().forEach(r -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + r.getRoleName()));
            log.debug("role added!! : {}", r.getRoleName());
        });
        log.debug("authorities: {}", authorities);
        return authorities;
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUsername() {
        return userInfoDto.email();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userInfoDto.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userInfoDto.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userInfoDto.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userInfoDto.isEnabled();
    }

}
