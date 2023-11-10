package challange_5.binarfud.model.dto;

import java.util.UUID;

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
