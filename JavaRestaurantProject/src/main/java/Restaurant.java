import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class
Restaurant {
    private static final Logger logger = LoggerFactory.getLogger(Restaurant.class);

    private static Long RESTAURANT_ID = 0L;

    private final long id;
    private final String name;
    private URL location;
    private double leaseCostPerSqFt;
    private double areaSqFt;
    private Menu menu;
    private List<Table> tables;
    private List<Waiter> waiters;
    private List<Chef> chefs;
    private Manager manager;
    private RestaurantStatus restaurantStatus;

    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

    public Restaurant(String name) {
        this.id = ++RESTAURANT_ID;
        this.name = name;
        restaurantStatus = RestaurantStatus.OPEN;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public URL getLocation() {
        return location;
    }

    public void setLocation(URL location) {
        this.location = location;
    }

    public double getLeaseCostPerSqFt() {
        return leaseCostPerSqFt;
    }

    public void setLeaseCostPerSqFt(double leaseCostPerSqFt) {
        this.leaseCostPerSqFt = leaseCostPerSqFt;
    }

    public double getAreaSqFt() {
        return areaSqFt;
    }

    public void setAreaSqFt(double areaSqFt) {
        this.areaSqFt = areaSqFt;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public List<Waiter> getWaiters() {
        return waiters;
    }

    public void setWaiters(List<Waiter> waiters) {
        this.waiters = waiters;
    }

    public List<Chef> getChefs() {
        return chefs;
    }

    public void setChefs(List<Chef> chefs) {
        this.chefs = chefs;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public BlockingQueue<Order> getOrderQueue() {
        return orderQueue;
    }

    public void startRestaurant() {
        ExecutorService executorService = Executors.newFixedThreadPool(getTables().size() +
                getChefs().size());

        List<Future<?>> futures = new ArrayList<>(getTables().size() + getChefs().size());

        logger.debug("Creating table threads...");
        for(Table table: getTables()) {
            futures.add(executorService.submit(table));
        }

        logger.debug("Creating chef threads...");
        for(Chef chef: getChefs()) {
            futures.add(executorService.submit(chef));
        }

        // let restaurant run for some time...5 secs for testing
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.debug("closing the restaurant now.");
        closeRestaurant();

        logger.debug("waiting for threads to terminate...restaurant-status:" + restaurantStatus);
        boolean staffWorking = true;
        try {
            while (restaurantStatus == RestaurantStatus.OPEN || staffWorking) {
                staffWorking = false;
                for (Future<?> future : futures) {
                    if (!future.isDone()) {
                        staffWorking = true;
                        break;
                    }
                }

                if (staffWorking) {
                    logger.debug("waiting some more time for staff to finish the work.");
                    executorService.awaitTermination(100, TimeUnit.MILLISECONDS);
                }
            }
        } catch (InterruptedException e) {
            logger.error("Interrupted Exception occurred", e);
        }

        if(restaurantStatus == RestaurantStatus.CLOSED && !staffWorking) {
            logger.debug("Restaurant is closed. All staff members stopped working...");
        } else {
            logger.warn("Closing the restaurant abruptly.");
        }
        executorService.shutdownNow();

        logger.debug("Manager is going to reconcile the data and generate report now...");

        boolean valid = manager.validate();
        if(valid) {
            logger.debug("all orders reconciled.");
        } else {
            logger.error("all orders are not reconciled. Please look at logs for errors in processing.");
        }

        manager.generateReport();
    }

    public void closeRestaurant() {
        restaurantStatus = RestaurantStatus.CLOSED;
        for(Table table: tables) {
            table.stopTakingOrders();
        }
        for(Chef chef: chefs) {
            chef.stopPreparingFood();
        }
    }
}
