package binarfud.challenge3.model;

import lombok.Setter;
import lombok.Getter;

public class Order {
    private @Getter @Setter Integer qty;
    private @Getter @Setter String notes;

    public Order(){
        notes = "";
    }
}
