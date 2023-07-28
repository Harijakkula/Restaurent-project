import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Table implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Table.class);

    private static int TABLE_ID = 0;
    private final int id;
    private final int numOfChairs;
    private Waiter waiter;
    private Manager manager;

    private boolean stopOrders = false;

    public Table(int numOfChairs) {
        this.numOfChairs = numOfChairs;
        this.id = ++TABLE_ID;
    }

    public int getId() {
        return id;
    }

    public int getNumOfChairs() {
        return numOfChairs;
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public Manager getManager() {
        return manager;
    }

    @Deprecated
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        while(!stopOrders) {
            // prepare order
            logger.debug("Preparing the order. table: " + id);
            Order order = new Order();
            order.addToOrder(FoodItem.STARTER, 2);
            order.addToOrder(FoodItem.MAINCOURSE, 2);
            order.addToOrder(FoodItem.DESSERT, 2);
            order.addToOrder(FoodItem.DRINK, 2);
            order.setTable(this);

            try {
                logger.debug("placing the order. order-id:{}, table:{}, waiter:{}", order.getId(), id, waiter.getName());
                waiter.placeOrder(order);

                logger.debug("waiting for the order to be delivered. order-id:{}, table:{}, waiter:{}", order.getId(), id, waiter.getName());
                synchronized (this) {
                    this.wait();
                }

                logger.debug("food delivered. order-id:{}, table:{}, waiter:{}", order.getId(), id, waiter.getName());
                deliverFood(order);

                logger.debug("eating now. order-id:{}, table:{}, waiter:{}", order.getId(), id, waiter.getName());
                Thread.sleep(100);
            } catch (InterruptedException e) {
                logger.error("Interrupted Exception occurred. msg:" + e.getMessage());
                stopOrders = true;
            }
        }

        logger.debug("Table {} stopped serving", getId());
    }

    public void deliverFood(final Order order) {
        manager.record(order);
    }

    public void stopTakingOrders() {
        logger.debug("Stopping new orders now. table-id:{}", getId());
        stopOrders = true;
    }
}
