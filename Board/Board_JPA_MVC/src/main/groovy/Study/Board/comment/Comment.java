package Study.Board.comment;

import Study.Board.board.Board;
import Study.Board.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString(exclude = "board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String comment;

    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Builder
    public Comment(String comment, String password) {
        this.comment = comment;
        this.password = password;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void encodePassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
