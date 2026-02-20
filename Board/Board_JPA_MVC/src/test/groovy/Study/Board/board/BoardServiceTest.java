package Study.Board.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Test
    void create() {
        // given
        Board board = new Board("pass123", "테스트 내용", "테스트 제목");
        Board saveBoard = boardRepository.save(board);

        // when

        // then
        assertThat(board.equals(saveBoard));

    }


    @Test
    void readAll() {
        // given

        // when

        // then
    }

    @Test
    void readDetail() {
        // given

        // when

        // then

    }

    @Test
    void update() {
        // given

        // when

        // then

    }

    @Test
    void delete() {
        // given

        // when

        // then

    }
}