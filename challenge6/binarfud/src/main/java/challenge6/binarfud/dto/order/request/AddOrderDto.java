package challenge6.binarfud.dto.order.request;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@EqualsAndHashCode
public class AddOrderDto {
    @NotNull
    private List<OrderData> data;
    @NotNull
    private String destination;

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    public static class OrderData {
        private String note;
        @Min(1)
        private Integer quantity;
        @NotNull
        private UUID productId;
    }

}
