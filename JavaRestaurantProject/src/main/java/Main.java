import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        Restaurant restaurant = createRestaurant();
        logger.debug("start the restaurant now.");
        restaurant.startRestaurant();
    }

    private static Restaurant createRestaurant() {
        Restaurant restaurant = new Restaurant("Mingi Chow");

        Menu menu = createMenu();
        restaurant.setMenu(menu);

        Manager manager = new Manager("Manager1");
        restaurant.setManager(manager);

        restaurant.setLeaseCostPerSqFt(10);
        restaurant.setAreaSqFt(1000);

        List<Waiter> waiters = createWaiters(restaurant.getOrderQueue());
        restaurant.setWaiters(waiters);

        List<Table> tables = createTables(waiters, manager);
        restaurant.setTables(tables);

        List<Chef> chefs = createChefs(restaurant.getOrderQueue());
        restaurant.setChefs(chefs);

        return restaurant;
    }

    private static Menu createMenu() {
        Menu menu = new Menu();
        menu.setStarter(FoodItem.STARTER);
        menu.setMainCourse(FoodItem.MAINCOURSE);
        menu.setDessert(FoodItem.DESSERT);
        menu.setDrink(FoodItem.DRINK);
        return menu;
    }

    private static List<Table> createTables(final List<Waiter> waiters, Manager manager) {
        Table table1 = new Table(4); table1.setWaiter(waiters.get(0)); table1.setManager(manager);
        Table table2 = new Table(4); table2.setWaiter(waiters.get(0)); table2.setManager(manager);
        Table table3 = new Table(4); table3.setWaiter(waiters.get(1)); table3.setManager(manager);
        Table table4 = new Table(4); table4.setWaiter(waiters.get(1)); table4.setManager(manager);
        Table table5 = new Table(4); table5.setWaiter(waiters.get(2)); table5.setManager(manager);
        Table table6 = new Table(4); table6.setWaiter(waiters.get(2)); table6.setManager(manager);
        Table table7 = new Table(4); table7.setWaiter(waiters.get(3)); table7.setManager(manager);
        Table table8 = new Table(4); table8.setWaiter(waiters.get(3)); table8.setManager(manager);
        return List.of(table1, table2, table3, table4, table5, table6, table7, table8);
    }

    private static List<Waiter> createWaiters(final BlockingQueue<Order> orderQueue) {
        Waiter waiter1 = new Waiter("waiter1", orderQueue);
        waiter1.setYearsOfExperience(3);

        Waiter waiter2 = new Waiter("waiter2", orderQueue);
        waiter1.setYearsOfExperience(4);

        Waiter waiter3 = new Waiter("waiter3", orderQueue);
        waiter1.setYearsOfExperience(5);

        Waiter waiter4 = new Waiter("waiter4", orderQueue);
        waiter1.setYearsOfExperience(6);

        return List.of(waiter1, waiter2, waiter3, waiter4);
    }

    private static List<Chef> createChefs(final BlockingQueue<Order> orderQueue) {
        Chef chef1 = new Chef("Chef1", orderQueue);
        chef1.setYearsOfExperience(5);

        Chef chef2 = new Chef("Chef2", orderQueue);
        chef1.setYearsOfExperience(5);

        Chef chef3 = new Chef("Chef3", orderQueue);
        chef1.setYearsOfExperience(5);

        Chef chef4 = new Chef("Chef4", orderQueue);
        chef1.setYearsOfExperience(5);

        return List.of(chef1, chef2, chef3, chef4);
    }
}
