package net.nonworkspace.demo.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.embeddable.WriteInfo;

@Entity
@Data
@SequenceGenerator(name = "comment_id_generator", sequenceName = "comment_comment_id_seq", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_id_generator")
    private Long commentId;

    private String content;

    @Embedded
    private WriteInfo writer = new WriteInfo();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;
    
    public void setBoard(Board board) {
        this.board = board;
        board.getComments().add(this);
    }

    public static Comment createComment(Long commentId, Board board, String content, Long writerId) {
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        comment.setBoard(board);
        comment.setContent(content);
        WriteInfo writer = new WriteInfo();
        writer.setWriterId(writerId);
        comment.setWriter(writer);
        return comment;
    }
}
