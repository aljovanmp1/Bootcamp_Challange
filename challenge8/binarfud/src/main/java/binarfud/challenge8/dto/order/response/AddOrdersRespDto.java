package binarfud.challenge8.dto.order.response;

import binarfud.challenge8.dto.product.response.ProductRespDto;
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
