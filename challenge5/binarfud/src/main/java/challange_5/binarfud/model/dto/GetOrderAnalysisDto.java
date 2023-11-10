package challange_5.binarfud.model.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderAnalysisDto {
    private UUID id;
    private String period; 
}
