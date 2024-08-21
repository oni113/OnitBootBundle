package net.nonworkspace.demo.domain.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.entity.Board;

@Schema(title = "BOARD_REQ_04 : 게시물 등록 DTO")
public record BoardFormDto(
    @Schema(description = "게시물 ID") Long boardId,
    @NotEmpty(message = "제목을 입력해주세요.") @Schema(description = "제목") String title,
    @NotEmpty(message = "내용을 입력해주세요.") @Schema(description = "내용") String content,
    @Schema(description = "작성일시") LocalDateTime createDate,
    @Schema(description = "수정일시") LocalDateTime modifiedDate
) {

    public BoardFormDto(String title, String content) {
        this(
            null,
            title,
            content,
            LocalDateTime.now(),
            null
        );
    }

    public BoardFormDto(Board board) {
        this(
            board.getBoardId(),
            board.getTitle(),
            board.getContent(),
            board.getWriter().getCreateDate(),
            board.getModifiedDate()
        );
    }

    public static io.swagger.v3.oas.models.media.Schema getSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>().type("object")
            .title("BOARD_REQ_04 : 게시물 등록 DTO")
            .addProperty("boardId", new NumberSchema().description("게시물 ID"))
            .addProperty("title", new StringSchema().description("제목"))
            .addProperty("content", new StringSchema().description("내용"))
            .addProperty("createDate", new DateTimeSchema().description("작성일시"))
            .addProperty("modifiedDate", new DateTimeSchema().description("수정일시"));
    }
}
