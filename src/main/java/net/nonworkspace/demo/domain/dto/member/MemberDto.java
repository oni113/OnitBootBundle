package net.nonworkspace.demo.domain.dto.member;

import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.EmailSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.entity.Member;

public record MemberDto(
    Long memberId,
    String name,
    String email,
    LocalDateTime createDate
) {

    public MemberDto(Member member) {
        this(
            member.getMemberId(),
            member.getName(),
            member.getEmail(),
            member.getCreateInfo().getCreateDate()
        );
    }

    public static Schema getSchema() {
        return new Schema<>().type("object")
            .title("MEMBER_REQ_01 : 회원 DTO")
            .addProperty("memberId", new NumberSchema().description("회원 ID"))
            .addProperty("name", new StringSchema().description("회원 이름"))
            .addProperty("email", new EmailSchema().description("회원 이메일"))
            .addProperty("createDate", new DateTimeSchema().description("등록일시"));
    }
}
