package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemDto {

    private String itemName;
    private Integer price;
    private Integer quantity;

}
