package challenge6.binarfud.dto.merchant.request;

import challenge6.binarfud.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMerchantsDto extends PageDto{
    Boolean open;
}
