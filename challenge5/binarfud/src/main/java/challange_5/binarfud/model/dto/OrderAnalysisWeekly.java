package challange_5.binarfud.model.dto;

import lombok.Getter;
import lombok.Setter;

public interface OrderAnalysisWeekly {
    Integer getPrice();

    Integer getQty();

    Integer getWeekCount();

    Integer getWeekMonth();

    Integer getMonthCount();

    String getMerchantName();
}
