package challange_5.binarfud.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import challange_5.binarfud.model.Product;
import challange_5.binarfud.model.OrderDetail;
import challange_5.binarfud.model.Order;
import challange_5.binarfud.repository.OrderDetailRepository;
import lombok.Getter;

@Service
public class OrderDetailService {

    @Autowired
    OrderDetailRepository orderDetailRepository;

    private @Getter LinkedHashMap<Long, OrderDetail> orderQty = new LinkedHashMap<>();
    
    public Integer getTotalItem() {
        return this.orderQty.values()
                .stream()
                .mapToInt(OrderDetail::getQuantity)
                .sum();
    }

    public Integer getTotalPrice() {
        return this.orderQty.entrySet()
                .stream()
                .mapToInt(order -> order.getValue().getTotalPrice())
                .sum();
    }

    public void clearOrder() {
        orderQty.clear();
    }

    public void saveOrderDetail(Order order) {
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Map.Entry<Long, OrderDetail> entry : orderQty.entrySet()){
            entry.getValue().setOrder(order);
            orderDetailList.add(entry.getValue());
        }
        orderDetailRepository.saveAll(orderDetailList);
    }

    public List<OrderDetail> saveOrderDetails(List<OrderDetail> orderDetailList, Order order) {
        for (OrderDetail detail : orderDetailList){
            detail.setOrder(order);
        }
        return orderDetailRepository.saveAll(orderDetailList);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(UUID orderId){
        return orderDetailRepository.findByOrderId(orderId);
    }
}
