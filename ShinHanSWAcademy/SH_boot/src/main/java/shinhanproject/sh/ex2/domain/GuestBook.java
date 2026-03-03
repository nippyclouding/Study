package shinhanproject.sh.ex2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@ToString
public class GuestBook extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String writer;

    @Builder // 2. 필요한 필드만 포함하는 생성자에 빌더 적용
    public GuestBook(String title, String content, String writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

}
