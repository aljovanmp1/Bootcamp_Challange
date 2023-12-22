package binarfud.challenge8.dto.merchant.others;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderAnalysisDto {
    private UUID merchantId;
    private String merchantName;
    private String merchantLocation;
    private List<Data> data;

    private Integer totalQty;
    private String totalPrice;

    @Setter
    @Getter
    public static class Data {
        private String period;
        private Integer qty;
        private Integer income;
    }


}
