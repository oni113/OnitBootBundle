package net.nonworkspace.demo.domain.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.Member;

@Schema(title = "MEMBER_REQ_01 : 회원 DTO")
public record MemberDto(
    @Schema(description = "회원 ID") Long memberId,
    @Schema(description = "회원 이름") String name,
    @Schema(description = "회원 이메일") String email,
    @Schema(description = "등록일시") LocalDateTime createDate
) {

    public MemberDto(Member member) {
        this(
            member.getMemberId(),
            member.getName(),
            member.getEmail(),
            member.getCreateInfo().getCreateDate()
        );
    }
}
