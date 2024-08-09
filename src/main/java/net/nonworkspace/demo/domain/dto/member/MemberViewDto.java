package net.nonworkspace.demo.domain.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.nonworkspace.demo.domain.Member;

@Schema(title = "MEMBER_REQ_01 : 회원 상세 DTO")
public record MemberViewDto(
    @Schema(description = "회원 ID") Long memberId,
    @Schema(description = "회원 이름") String name,
    @Schema(description = "회원 이메일") String email,
    @Schema(description = "등록일시") LocalDateTime createDate,
    @Schema(description = "권한") List<RoleDto> roles
) {

    public MemberViewDto(Member member) {
        this(
            member.getMemberId(),
            member.getName(),
            member.getEmail(),
            member.getCreateInfo().getCreateDate(),
            new ArrayList<>() {
                @Override
                public RoleDto get(final int index) {
                    return new RoleDto(
                        member.getRoles().get(index)
                    );
                }

                @Override
                public int size() {
                    return member.getRoles().size();
                }
            }
        );
    }
}
