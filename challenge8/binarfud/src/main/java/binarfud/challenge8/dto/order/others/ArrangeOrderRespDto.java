package binarfud.challenge8.dto.order.others;

import java.util.List;

import binarfud.challenge8.model.Order;
import binarfud.challenge8.model.OrderDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArrangeOrderRespDto {
    List<OrderDetail> orderDetailList;
    Order order;
}
