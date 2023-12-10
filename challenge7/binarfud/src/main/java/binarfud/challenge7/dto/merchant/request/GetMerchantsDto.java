package binarfud.challenge7.dto.merchant.request;

import binarfud.challenge7.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMerchantsDto extends PageDto{
    Boolean open;
}
