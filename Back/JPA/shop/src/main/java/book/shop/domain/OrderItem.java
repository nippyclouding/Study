package book.shop.domain;

import book.shop.domain.item.Item;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "orders_item_id")
    private Long id;

    private Long totalPrice;

    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;



    // 연관관계 편의 메서드

    public void addItem (Item item) {
        this.item = item;
        item.getOrderItems().add(this);
    }

    public void setOrder(Order order) {
        this.order = order;
        order.getOrderItems().add(this);
    }
}
