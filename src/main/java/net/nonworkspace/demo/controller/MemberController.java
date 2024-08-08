package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Member;
import net.nonworkspace.demo.model.MemberVO;
import net.nonworkspace.demo.service.MemberJpaService;
import net.nonworkspace.demo.service.MemberService;

@Tag(name = "회원 API", description = "회원 정보를 처리하는 API 설명")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
// TODO : convert return Object: Entity to Dto
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
                schema = @Schema(implementation = MemberVO.class),
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
    public List<MemberVO> getMemberList(@RequestParam(name = "name", required = false) String name) {
        MemberVO member = new MemberVO();
        member.setName(name);
        // return memberJpaService.findMembers(name);
        return memberService.findMembers(name);
    }

    @Operation(summary = "회원 등록", description = "회원 데이터를 등록한다.")
    @PostMapping("/new")
    public Long postMember(@RequestBody MemberVO member) {
        // return memberJpaService.join(member);
        return memberService.join(member);
    }

    @Operation(summary = "회원 조회)", description = "회원 정보를 조회한다.")
    @Parameter(name = "memberId", description = "회원 ID")
    @GetMapping("/{memberId}")
    public Optional<Member> getMemember(@PathVariable(name = "memberId", required = true) Long memberId) {
        return Optional.of(memberJpaService.findMember(memberId));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 데이터를 수정한다.")
    @Parameter(name = "memberId", description = "회원 ID")
    @PutMapping("/{memberId}")
    public Member editMember(@PathVariable(name = "memberId", required = true) Long memberId,
            @RequestBody Member member) {
        member.setMemberId(memberId);
        return memberJpaService.editMember(member);
    }

    @Operation(summary = "회원 삭제", description = "회원 데이터를 삭제한다.")
    @Parameter(name = "memberId", description = "회원 ID")
    @DeleteMapping("/{memberId}")
    public int deleteMember(@PathVariable(name = "memberId", required = true) Long memberId) {
        return memberJpaService.deleteMember(memberId);
    }
}
