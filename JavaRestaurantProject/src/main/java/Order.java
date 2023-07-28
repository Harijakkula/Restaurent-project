import java.util.HashMap;
import java.util.Map;

public class Order {
    private static int ORDER_ID = 0;

    private final int id;
    private final Map<FoodItem, Integer> orderItems = new HashMap<>();
    private Table table;
    private Chef chef;

    public Order() {
        this.id = ++ORDER_ID;
    }

    public int getId() {
        return id;
    }

    public Order addToOrder(FoodItem foodItem, int count) {
        orderItems.put(foodItem, count);
        return this;
    }

    public Table getTable() {
        return this.table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Map<FoodItem, Integer> getOrderItems() {
        return orderItems;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }
}
