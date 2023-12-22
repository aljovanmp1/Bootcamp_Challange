package binarfud.challenge8.dto.product.request;

import lombok.Getter;

@Getter
public class PutProductDto {
    private String productName;
    private Integer price;
    private boolean isDeleted;
}
