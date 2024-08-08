package net.nonworkspace.demo.domain.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.Board;

@Schema(title = "BOARD_REQ_01 : 게시물 목록 DTO")
public record BoardDto(
    @NotNull @Schema(description = "게시물 ID") Long boardId,
    @NotEmpty @Schema(description = "제목") String title,
    @NotNull @Schema(description = "작성자 ID") Long writerId,
    @NotNull @Schema(description = "작성일시") LocalDateTime createDate,
    @Schema(description = "댓글 수") int commentCount
) {
    public BoardDto(Board board) {
        this(
            board.getBoardId(),
            board.getTitle(),
            board.getWriter().getWriterId(),
            board.getWriter().getCreateDate(),
            board.getComments().size()
        );
    }
}
