package book.shop.domain.item;

import book.shop.domain.BaseEntity;
import book.shop.domain.CategoryItem;
import book.shop.domain.OrderItem;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item extends BaseEntity {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Long price;

    private String imgUrl;

    private int stockQuantity;

    @OneToMany(mappedBy = "item") // 기본값 = lazy load
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany(mappedBy = "item") // 기본값 = lazy load
    private List<CategoryItem> categoryItems = new ArrayList<>();
}
