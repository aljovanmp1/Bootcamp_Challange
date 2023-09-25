package binarfud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
 
@AllArgsConstructor
public class Menu {
    private @Getter int id;
    private @Getter String name;
    private @Getter int price;
}
