package book.shop.domain;

import book.shop.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class WishList extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "wishList_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MyPage myPage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime createdAt;  // 찜한 시간
}
