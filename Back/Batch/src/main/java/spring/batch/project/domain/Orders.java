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
@AllArgsConstructor
public class Orders {
    @Id @GeneratedValue
    private Long id;

    private String customerName;

    private String storeName;

    private Integer amount;

    private LocalDate orderDate; // yyyy-mm-dd
}
