package challange_5.binarfud.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AddProductDto {
    private String productName;
    @Min(0)
    private Integer price;
    @NotNull
    private UUID merchantId;
}
