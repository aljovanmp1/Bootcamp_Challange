package binarfud.challenge7.dto.product.request;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AddProductDto {
    private String productName;
    @Min(0)
    private Integer price;
}
