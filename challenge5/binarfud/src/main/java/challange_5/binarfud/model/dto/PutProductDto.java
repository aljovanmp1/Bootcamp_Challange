package challange_5.binarfud.model.dto;

import java.util.UUID;

import lombok.Getter;

@Getter
public class PutProductDto {
    private String productName;
    private Integer price;
    private UUID merchantId;
    private boolean isDeleted;
}
