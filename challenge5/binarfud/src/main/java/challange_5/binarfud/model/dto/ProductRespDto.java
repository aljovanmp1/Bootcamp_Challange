package challange_5.binarfud.model.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductRespDto {
    private UUID id;
    private String productName;
    private Integer price;
    private boolean isDeleted;
    private UUID merchantId;
}
