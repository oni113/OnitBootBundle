package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.common.CommonResponseDto;
import net.nonworkspace.demo.domain.dto.member.MemberDto;
import net.nonworkspace.demo.domain.dto.member.MemberViewDto;
import net.nonworkspace.demo.service.MemberJpaService;
import net.nonworkspace.demo.service.MemberService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원 API", description = "회원 정보를 처리하는 API 설명")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/member")
@Slf4j
public class MemberController {

    public final MemberService memberService;

    public final MemberJpaService memberJpaService;

    @Operation(summary = "회원 리스트 조회", description = "회원 전체 데이터를 리스트로 조회한다.")
    @Parameter(name = "name", description = "이름에 값을 포함하는 문자열")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "회원 목록",
            content = @Content(
                schema = @Schema(implementation = MemberDto.class),
                examples = @ExampleObject(
                    value = """
                        [
                            {
                                "memberId": 1,
                                "name": "John",
                                "email": "john@gmail.com",
                                "createDate": "2020-01-01",
                            },
                            {
                                "memberId": 2,
                                "name": "김누구",
                                "email": "kim@gmail.com",
                                "createDate": "2020-01-01",
                            }
                        ]
                        """

                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal Server Error"
        )
    })
    @GetMapping("")
    public List<MemberDto> getMemberPage(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization") String token,
        @RequestParam(name = "pageNo", defaultValue = "1") int pageNo,
        @RequestParam(name = "pageSize", required = false, defaultValue = "6") int pageSize,
        @RequestParam(name = "name", required = false) String name) {
        return memberJpaService.getPage(name, pageNo, pageSize);
    }

    @Operation(summary = "회원 조회)", description = "회원 정보를 조회한다.")
    @Parameter(name = "memberId", description = "회원 ID")
    @GetMapping("/{memberId}")
    public MemberViewDto getMember(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization") String token,
        @PathVariable(name = "memberId") Long memberId) {
        return memberJpaService.findMember(memberId);
    }

    @Operation(summary = "회원 삭제", description = "회원 데이터를 삭제한다.")
    @Parameter(name = "memberId", description = "회원 ID")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponseDto> deleteMember(@Parameter(hidden = true) @RequestHeader(
        name = "Authorization") String token, @PathVariable(name = "memberId") Long memberId) {
        try {
            return ResponseEntity.ok(
                new CommonResponseDto(
                    memberJpaService.deleteMember(memberId),
                    "회원 삭제 성공"
                )
            );
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseDto(-1L, "삭제 실패: " + e.getMessage()));
        }
    }
}
