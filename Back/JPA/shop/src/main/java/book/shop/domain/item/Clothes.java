package book.shop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@DiscriminatorValue("C")
@Getter
public class Clothes extends Item{
    @Enumerated(EnumType.STRING)
    private ClothingSize size;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
