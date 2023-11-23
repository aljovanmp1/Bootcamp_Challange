package challenge6.binarfud.dto.product.request;

import java.util.UUID;

import challenge6.binarfud.dto.PageDto;
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
