package challenge6.binarfud.dto.product.request;

import lombok.Getter;

@Getter
public class PutProductDto {
    private String productName;
    private Integer price;
    private boolean isDeleted;
}
