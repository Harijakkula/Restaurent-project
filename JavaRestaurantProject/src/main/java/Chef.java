import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Chef extends Person implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Chef.class);

    private int yearsOfExperience;
    private BlockingQueue<Order> orderQueue;
    private boolean cookFood = true;

    public Chef(String name, BlockingQueue<Order> orderQueue) {
        super(name);
        this.orderQueue = orderQueue;
    }

    public Chef(String name, String phoneNumber, String email) {
        super(name, phoneNumber, email);
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public void run() {
        while(cookFood) {
            try {
                logger.debug("Polling the queue for the order. chef: {}", getName());
                Order order = orderQueue.poll(10, TimeUnit.MILLISECONDS);
                if(order == null) {
                    logger.debug("No order polled, sleeping for sometime. chef:{}", getName());
                    Thread.sleep(100);
                } else {
                    order.setChef(this);
                    logger.debug("Got the order. order-id:{}, table-id:{}, waiter:{}. Cooking the food now. chef:{}",
                            order.getId(), order.getTable().getId(), order.getTable().getWaiter().getName(), getName());
                    Thread.sleep(100);
                    logger.debug("Food is cooked. Notifying the waiter. order-id:{}, table-id:{}, waiter:{}. Cooking the food now. chef:{}",
                            order.getId(), order.getTable().getId(), order.getTable().getWaiter().getName(), getName());

                    synchronized (order.getTable()) {
                        order.getTable().notify();
                    }
                }
            } catch (InterruptedException e) {
                logger.error("Thread interrupted.", e);
                cookFood = false;
            }
        }
    }

    public void stopPreparingFood() {
        cookFood = false;
    }
}
