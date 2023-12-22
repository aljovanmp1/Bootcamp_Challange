package binarfud.challenge8.dto.merchant.request;

import binarfud.challenge8.dto.PageDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetMerchantsDto extends PageDto{
    Boolean open;
}
