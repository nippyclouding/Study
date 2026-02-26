package shinhanproject.sh.entity;

import jakarta.persistence.*;

@Entity
public class Memo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(length = 200, nullable = false)
    private String text;
}
