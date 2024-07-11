package net.nonworkspace.demo.domain.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDto {

    private Long userId;

    private String email;

    private String name;

    private String password;

    private List<Role> roles = new ArrayList<Role>();
}
