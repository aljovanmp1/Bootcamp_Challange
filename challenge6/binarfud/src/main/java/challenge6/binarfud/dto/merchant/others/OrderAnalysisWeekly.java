package challenge6.binarfud.dto.merchant.others;

public interface OrderAnalysisWeekly {
    Integer getPrice();

    Integer getQty();

    Integer getWeekCount();

    Integer getWeekMonth();

    Integer getMonthCount();

    String getMerchantName();
}
