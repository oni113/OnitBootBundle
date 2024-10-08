package net.nonworkspace.demo.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nonworkspace.demo.domain.entity.Board;
import net.nonworkspace.demo.domain.entity.Comment;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public Long save(Board board) {
        em.persist(board);
        return board.getBoardId();
    }

    public Long saveComment(Comment comment) {
        em.persist(comment);
        return comment.getCommentId();
    }

    public List<Board> findAll() {
        String query = "select b from Board b order by b.boardId desc";
        return em.createQuery(query, Board.class).getResultList();
    }

    public List<Board> findAll(String title) {
        String query =
            "select b from Board b where b.title like CONCAT('%', :title, '%') order by b.boardId desc";
        List<Board> result =
            em.createQuery(query, Board.class).setParameter("title", title).getResultList();
        return result;
    }

    public Board find(Long boardId) {
        return em.find(Board.class, boardId);
    }

    public void delete(Long boardId) {
        Board board = em.find(Board.class, boardId);
        Query query = em.createQuery("delete from Comment c where c.board.boardId = :boardId");
        query.setParameter("boardId", board.getBoardId()).executeUpdate();
        em.remove(board);
    }

    public Comment findComment(Long boardId, Long commentId) {
        Board board = em.find(Board.class, boardId);
        return board.getComments().stream().filter(c -> c.getCommentId().equals(commentId)).findAny()
            .orElse(null);
    }

    public void deleteComment(Long boardId, Long commentId) {
        Comment comment = em.find(Comment.class, commentId);
        if (comment.getBoard().getBoardId().equals(boardId)) {
            em.remove(comment);
        }
    }
}
