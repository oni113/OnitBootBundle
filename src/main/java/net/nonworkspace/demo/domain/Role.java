package net.nonworkspace.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    @Schema(description = "권한 ID")
    private Long id;

    @Schema(description = "권한 이름")
    private String roleName;

    @ManyToOne(/* cascade = CascadeType.ALL, */ fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    private Member member;

    public void setMember(Member member) {
        this.member = member;
        member.getRoles().add(this);
    }

    public static Role createRole(Member member, String roleName) {
        Role role = new Role();
        role.setMember(member);
        role.setRoleName(roleName);
        return role;
    }
}
