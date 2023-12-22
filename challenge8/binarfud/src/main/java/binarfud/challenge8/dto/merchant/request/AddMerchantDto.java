package binarfud.challenge8.dto.merchant.request;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class AddMerchantDto {
    @NotNull
    private String merchantName;
    @NotNull
    private String merchantLocation;
}
