package binarfud.challenge8.dto.product.response;

import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AddProductRespDto {
    private UUID id;
    private String productName;
    private Integer price;
    private UUID merchantId;
}
