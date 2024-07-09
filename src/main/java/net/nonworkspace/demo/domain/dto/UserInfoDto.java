package net.nonworkspace.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long userId;

    private String email;

    private String name;

    private String password;
    
    private String token;
}
