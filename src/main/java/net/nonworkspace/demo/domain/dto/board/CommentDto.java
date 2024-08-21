package net.nonworkspace.demo.domain.dto.board;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.NumberSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import net.nonworkspace.demo.domain.entity.Comment;

@Schema(title = "BOARD_REQ_03 : 댓글 DTO")
public record CommentDto(
    @Schema(description = "댓글 ID") Long commentId,
    @NotEmpty(message = "내용을 입력해주세요.") @Schema(description = "내용") String content,
    @Schema(description = "작성자 ID") Long writerId,
    @Schema(description = "작성일시") LocalDateTime createDate
) {

    public CommentDto(Comment comment) {
        this(
            comment.getCommentId(),
            comment.getContent(),
            comment.getWriter().getWriterId(),
            comment.getWriter().getCreateDate()
        );
    }

    public static io.swagger.v3.oas.models.media.Schema getSchema() {
        return new io.swagger.v3.oas.models.media.Schema<>().type("object")
            .title("BOARD_REQ_03 : 댓글 DTO")
            .addProperty("commentId", new NumberSchema().description("댓글 ID"))
            .addProperty("content", new StringSchema().description("내용"))
            .addProperty("writerId", new NumberSchema().description("작성자 ID"))
            .addProperty("createDate", new DateTimeSchema().description("작성일시"));
    }
}
