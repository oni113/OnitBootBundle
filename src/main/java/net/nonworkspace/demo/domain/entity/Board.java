package net.nonworkspace.demo.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.nonworkspace.demo.domain.embeddable.WriteInfo;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Entity
@SequenceGenerator(name = "board_id_generator", sequenceName = "board_board_id_seq", allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_id_generator")
    private Long boardId;

    private String title;

    private String content;

    @Embedded
    private WriteInfo writer = new WriteInfo();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "수정일시")
    private LocalDateTime modifiedDate;

    @JsonIgnoreProperties({"board"})
    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public static Board createBoard(String title, String content, Long writerId) {
        Board board = new Board();
        board.title = title;
        board.content = content;
        board.writer = WriteInfo.create(writerId);
        return board;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
