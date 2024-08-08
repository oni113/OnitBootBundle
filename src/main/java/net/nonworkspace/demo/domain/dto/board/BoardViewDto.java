package net.nonworkspace.demo.domain.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import net.nonworkspace.demo.domain.Board;

@Schema(title = "BOARD_REQ_02 : 게시물 DTO")
public record BoardViewDto(
    @Schema(description = "게시물 ID") Long boardId,
    @NotEmpty @Schema(description = "제목") String title,
    @NotEmpty @Schema(description = "내용") String content,
    @NotNull @Schema(description = "작성자 ID") Long writerId,
    @NotNull @Schema(description = "작성일시") LocalDateTime createDate,
    @Schema(description = "수정일시") LocalDateTime modifiedDate,
    @Schema(description = "댓글") List<CommentDto> comments
) {
    public BoardViewDto(Board board) {
        this(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            board.getWriter().getWriterId(),
            board.getWriter().getCreateDate(),
            board.getModifiedDate(),
            new ArrayList<>() {
                @Override
                public CommentDto get(final int index) {
                    return new CommentDto(
                        board.getComments().get(index)
                    );
                }
                @Override
                public int size() {
                    return board.getComments().size();
                }
            }
        );
    }
}
