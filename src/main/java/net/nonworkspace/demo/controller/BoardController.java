package net.nonworkspace.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.nonworkspace.demo.domain.Board;
import net.nonworkspace.demo.domain.Comment;
import net.nonworkspace.demo.service.BoardService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BOARD API", description = "게시판 정보를 처리하는 API 설명")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 리스트 조회", description = "게시판 전체 데이터를 리스트로 조회한다.")
    @Parameter(name = "title", description = "제목에 값을 포함하는 문자열")
    @GetMapping("")
    public List<Board> getBoardList(@RequestParam(name = "title", required = false) String title) {
        Board board = new Board();
        board.setTitle(title);
        return boardService.findBoards(title);
    }

    @Operation(summary = "게시물 조회", description = "게시물 단건 데이터를 조회한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @GetMapping("/{boardId}")
    public Board getBoard(@PathVariable(name = "boardId", required = true) Long boardId) {
        return boardService.findBoard(boardId);
    }

    @Operation(summary = "게시물 등록", description = "게시물 데이터를 등록한다.")
    @PostMapping("/new")
    public Long postBoard(@RequestBody Board boarad) {
        return boardService.post(boarad);
    }

    @Operation(summary = "댓글 등록", description = "게시물의 댓글 데이터를 등록한다.")
    @PostMapping("/{boardId}/comment")
    public Long postBoard(@PathVariable(name = "boardId") Long boardId,
        @RequestBody Comment comment) {
        return boardService.postComment(boardId, comment);
    }

    @Operation(summary = "댓글 삭제", description = "댓글 데이터를 삭제한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @Parameter(name = "commentId", description = "댓글 ID")
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public int deleteComment(@PathVariable(name = "boardId", required = true) Long boardId,
        @PathVariable(name = "commentId", required = true) Long commentId) {
        return boardService.deleteComment(boardId, commentId);
    }

    @Operation(summary = "게시물 삭제", description = "게시물 데이터를 삭제한다.")
    @Parameter(name = "boardId", description = "게시물 ID")
    @DeleteMapping("/{boardId}")
    public int deleteBoard(@PathVariable(name = "boardId", required = true) Long boardId) {
        return boardService.deleteBoard(boardId);
    }
}
