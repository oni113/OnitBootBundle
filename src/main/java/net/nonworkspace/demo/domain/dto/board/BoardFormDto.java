package net.nonworkspace.demo.domain.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.Board;

@Schema(title = "BOARD_REQ_04 : 게시물 등록 DTO")
public record BoardFormDto(
    @Schema(description = "게시물 ID") Long boardId,
    @NotEmpty(message = "제목을 입력해주세요.") @Schema(description = "제목") String title,
    @NotEmpty(message = "내용을 입력해주세요.") @Schema(description = "내용") String content,
    @NotNull(message = "작성자 ID를 입력해주세요.") @Schema(description = "작성자 ID") Long writerId,
    @Schema(description = "작성일시") LocalDateTime createDate,
    @Schema(description = "수정일시") LocalDateTime modifiedDate
) {

    public BoardFormDto(Board board) {
        this(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            board.getWriter().getWriterId(),
            board.getWriter().getCreateDate(),
            board.getModifiedDate()
        );
    }
}
