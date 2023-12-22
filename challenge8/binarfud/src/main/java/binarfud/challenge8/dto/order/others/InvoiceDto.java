package binarfud.challenge8.dto.order.others;

import java.util.List;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceDto {
    private UUID orderId;
    private String userName;
    private String orderTime;
    private String dest;
    private int totalQty;
    private String totalPrice;
    private List<OrderPerMerchant> orders;

    @Getter
    @Setter
    public static class OrderPerMerchant{
        private String merchantName;
        private List<OrderDetailWithProduct> orderData;

        @Getter
        @Setter
        public static class OrderDetailWithProduct{
            private int num;
            private String productName;
            private String price;
            private int qty;
            private String total;
        }
    }

}
