package Study.Board.board;


import Study.Board.board.dtos.BoardUpdateReqDto;
import Study.Board.comment.Comment;
import Study.Board.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private String title;

    private String content;

    private String password;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true) // 1:N 은 기본값이 지연 로딩
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Board(String password, String title, String content) {
        this.password = password;
        this.title = title;
        this.content = content;
    }

    public void update(BoardUpdateReqDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        if (comment.getBoard() != this) {
            comment.setBoard(this);
        }
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
