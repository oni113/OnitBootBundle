package net.nonworkspace.demo.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class MemberVO {

    @Schema(description = "회원 ID (자동생성)")
    private Long memberId;

    @Schema(description = "회원 이름")
    private String name;

    @Schema(description = "회원 이메일")
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "등록일시")
    private LocalDateTime createDate;

    @Schema(description = "회원 패스워드")
    private String memberPassword;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "패스워드 만료일시")
    private LocalDateTime expireDate;
}
