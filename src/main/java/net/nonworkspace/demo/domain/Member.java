package net.nonworkspace.demo.domain;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.embeddable.CreateInfo;

@Data
@Entity
@SequenceGenerator(name = "member_id_generator", sequenceName = "member_member_id_seq",
        initialValue = 1, allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_id_generator")
    @Schema(description = "회원 ID (자동생성)")
    private Long memberId;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 이메일")
    private String email;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    @JsonIgnoreProperties({"member"})
    @OneToMany(mappedBy = "member", /* cascade = CascadeType.ALL, */ fetch = FetchType.LAZY)
    private List<Password> passwords = new ArrayList<>();
}
