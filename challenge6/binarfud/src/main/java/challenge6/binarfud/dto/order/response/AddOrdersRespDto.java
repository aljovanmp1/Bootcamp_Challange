package challenge6.binarfud.dto.order.response;

import challenge6.binarfud.dto.product.response.ProductRespDto;
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
