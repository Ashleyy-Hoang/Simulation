package cmsc433;

import java.util.List;

public class Order {
    
    public int orderNumber;
    public String customerName;
    public List<Food> order;

    public Order(int orderNumber, String customerName, List<Food> order){
        this.orderNumber = orderNumber;
        this.customerName = customerName;
        this.order = order;
    }
}
