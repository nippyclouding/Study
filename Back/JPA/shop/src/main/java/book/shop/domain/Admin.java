package book.shop.domain;

import book.shop.domain.item.Item;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Admin {
    @Id @GeneratedValue
    @Column(name = "admin_id")
    private Long id;

    private String loginId;
    private String loginPassword;

    @OneToMany
    private List<Category> categories = new ArrayList<>();

    @OneToMany
    private List<Item> items = new ArrayList<>();

    @OneToMany
    private List<Member> members = new ArrayList<>();

    @OneToMany
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToMany
    private List<Order> orders = new ArrayList<>();
}
