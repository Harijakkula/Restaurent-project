import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Waiter extends Person {
    private int yearsOfExperience;
    private BlockingQueue<Order> orderQueue;

    public Waiter(String name, final BlockingQueue<Order> orderQueue) {
        super(name);
        this.orderQueue = orderQueue;
    }

    public Waiter(String name, String phoneNumber, String email) {
        super(name, phoneNumber, email);
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(final int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public synchronized boolean placeOrder(final Order order) throws InterruptedException {
        return orderQueue.offer(order, 10, TimeUnit.MILLISECONDS);
    }
}
