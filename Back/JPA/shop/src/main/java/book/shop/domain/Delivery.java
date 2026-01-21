package book.shop.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Delivery extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; // 대기, 이동 중, 도착

    @Embedded
    private Address address;


    // Delivery는 Order을 가지지 않는다 : 단방향 설계
}
