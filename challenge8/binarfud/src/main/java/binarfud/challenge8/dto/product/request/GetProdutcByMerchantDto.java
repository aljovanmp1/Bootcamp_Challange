package binarfud.challenge8.dto.product.request;

import java.util.UUID;

import binarfud.challenge8.dto.PageDto;
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
