package binarfud.model;

import lombok.Setter;
import lombok.Getter;

public class Order {
    private @Getter @Setter int qty;
    private @Getter @Setter String notes;

    public Order(){
        notes = "";
    }
}
