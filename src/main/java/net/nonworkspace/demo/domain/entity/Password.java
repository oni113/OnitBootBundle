package net.nonworkspace.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.embeddable.CreateInfo;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Table(name = "member_password")
@SequenceGenerator(name = "password_id_generator",
    sequenceName = "member_password_member_password_id_seq", allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password {

    @Id
    @Column(name = "member_password_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_id_generator")
    @Schema(description = "회원 패스워드 ID (자동생성)")
    private Long memberPasswordId;

    @ManyToOne(/* cascade = CascadeType.ALL, */ fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Schema(description = "회원 패스워드")
    @Column(name = "member_password")
    private String memberPassword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "패스워드 만료일시")
    private LocalDateTime expireDate;

    @Embedded
    private CreateInfo createInfo = new CreateInfo();

    public void setMember(Member member) {
        this.member = member;
        member.getPasswords().add(this);
    }

    public static Password createPassword(Member member, String encodedPassword) {
        Password password = new Password();
        password.setMember(member);
        password.memberPassword = encodedPassword;
        password.expireDate = LocalDateTime.now().plusMonths(6);
        password.createInfo = new CreateInfo();
        return password;
    }
}
