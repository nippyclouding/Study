package book.shop.domain;

import book.shop.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CategoryItem {

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}
