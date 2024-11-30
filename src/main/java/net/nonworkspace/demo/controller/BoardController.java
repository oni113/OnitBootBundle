package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.dto.board.BoardDto;
import net.nonworkspace.demo.domain.dto.board.BoardFormDto;
import net.nonworkspace.demo.domain.dto.board.BoardViewDto;
import net.nonworkspace.demo.domain.dto.board.CommentDto;
import net.nonworkspace.demo.domain.dto.common.CommonResponseDto;
import net.nonworkspace.demo.domain.dto.common.ListResponse;
import net.nonworkspace.demo.service.AuthenticationService;
import net.nonworkspace.demo.service.BoardService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "BOARD API", description = "게시판 정보를 처리하는 API 설명")
@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 리스트 조회", description = "게시판 전체 데이터를 리스트로 조회한다.")
    @Parameter(name = "title", description = "제목에 값을 포함하는 문자열")
    @GetMapping("/api/board")
    public ListResponse<BoardDto> getBoardList(
            @RequestParam(name = "title", required = false) String title) {
        return boardService.findBoards(title);
    }

    @Operation(summary = "게시물 조회", description = "게시물 단건 데이터를 조회한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @GetMapping("/api/board/{boardId}")
    public BoardViewDto getBoard(@PathVariable(name = "boardId") Long boardId) {
        return boardService.findBoard(boardId);
    }

    @Operation(summary = "게시물 등록", description = "게시물 데이터를 등록한다.")
    @PostMapping("/user/board/new")
    public ResponseEntity<CommonResponseDto> postBoard(
            @Valid @RequestBody BoardFormDto boardFormDto, @Parameter(hidden = true) @RequestHeader(
            name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(new CommonResponseDto(
                    boardService.post(boardFormDto, AuthenticationService.getLoginUserInfo()),
                    "게시물 등록 성공"
            ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CommonResponseDto(-1L, e.getMessage())
            );
        }
    }

    @Operation(summary = "댓글 등록", description = "게시물의 댓글 데이터를 등록한다.")
    @PostMapping("/user/board/{boardId}/comment")
    public ResponseEntity<CommonResponseDto> postComment(
            @PathVariable(name = "boardId") Long boardId,
            @Valid @RequestBody CommentDto commentDto, @Parameter(hidden = true) @RequestHeader(
            name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(new CommonResponseDto(
                    boardService.postComment(boardId, commentDto,
                            AuthenticationService.getLoginUserInfo()),
                    "댓글 등록 성공"
            ));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CommonResponseDto(-1L, e.getMessage())
            );
        }
    }

    @Operation(summary = "댓글 삭제", description = "댓글 데이터를 삭제한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @Parameter(name = "commentId", description = "댓글 ID")
    @DeleteMapping("/user/board/{boardId}/comment/{commentId}")
    public ResponseEntity<CommonResponseDto> deleteComment(
            @PathVariable(name = "boardId") Long boardId,
            @PathVariable(name = "commentId") Long commentId, @Parameter(hidden = true) @RequestHeader(
            name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(
                    new CommonResponseDto(boardService.deleteComment(boardId, commentId,
                            AuthenticationService.getLoginUserInfo()),
                            "댓글 삭제 성공"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CommonResponseDto(-1L, e.getMessage())
            );
        }
    }

    @Operation(summary = "게시물 삭제", description = "게시물 데이터를 삭제한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @DeleteMapping("/user/board/{boardId}")
    public ResponseEntity<CommonResponseDto> deleteBoard(
            @PathVariable(name = "boardId") Long boardId, @Parameter(hidden = true) @RequestHeader(
            name = "Authorization") String token) {
        try {
            return ResponseEntity.ok(
                    new CommonResponseDto(
                            boardService.deleteBoard(boardId, AuthenticationService.getLoginUserInfo()),
                            "게시물 삭제 성공"));
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new CommonResponseDto(-1L, e.getMessage())
            );
        }
    }
}
