package spring.batch.project.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Settlement {
    @Id @GeneratedValue
    private Long id;

    private Long orderId; // 연관관계 매핑 생략

    private String storeName;

    private Integer settlementAmount; // 수수료를 반영한 정산 금액

    private LocalDate settlementDate; // 정산 처리일

    public Settlement(Long orderId, String storeName, Integer settlementAmount, LocalDate settlementDate) {
        this.orderId = orderId;
        this.storeName = storeName;
        this.settlementAmount = settlementAmount;
        this.settlementDate = settlementDate;
    }
}
