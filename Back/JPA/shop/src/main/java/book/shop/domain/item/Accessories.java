package book.shop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
@DiscriminatorValue("A")
@Getter
public class Accessories extends Item{
    private String accessorySize;
}
