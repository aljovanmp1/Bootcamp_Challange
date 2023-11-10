package challange_5.binarfud.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddOrdersRespDto {
    private ProductRespDto product;
    private int quantity;
    private String note;
    private int totalPrice;
}
