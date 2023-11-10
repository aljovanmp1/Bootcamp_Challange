package challange_5.binarfud.model.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class GetProdutcByMerchantDto extends PageDto{
    @NotNull
    UUID merchantId;
}
