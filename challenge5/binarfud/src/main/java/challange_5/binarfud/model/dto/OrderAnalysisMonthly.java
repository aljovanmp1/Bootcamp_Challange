package challange_5.binarfud.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

// @Getter
// @Setter
// @Data
// @AllArgsConstructor
public interface OrderAnalysisMonthly {
    Integer getPrice();

    Integer getQty();

    Integer getMonthCount();

    String getMerchantName();
}
