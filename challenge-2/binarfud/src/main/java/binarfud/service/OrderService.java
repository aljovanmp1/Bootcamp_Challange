package binarfud.service;

import java.util.LinkedHashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class OrderService {
    private @Getter LinkedHashMap<Integer, Integer> orderQty =  new LinkedHashMap<>();

    public void addOrder(Integer menuId, Integer qty) {
        orderQty.put(menuId, qty);
    }

}
