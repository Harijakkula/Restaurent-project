import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Manager extends Person {
    private static final Logger logger = LoggerFactory.getLogger(Manager.class);

    private int yearsOfExperience;

    private final Map<Table, AtomicInteger> tableOrdersMap = new ConcurrentHashMap<>();
    private final Map<Waiter, AtomicInteger> waiterOrdersMap = new ConcurrentHashMap<>();
    private final Map<Chef, AtomicInteger> chefOrdersMap = new ConcurrentHashMap<>();

    private final Set<Order> processedOrders = ConcurrentHashMap.newKeySet();

    private final ReportGenerator reportGenerator = new HtmlReportGenerator();

    public Manager(String name) {
        super(name);
    }

    public Manager(String name, String phoneNumber, String email) {
        super(name, phoneNumber, email);
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    /*
        thread safe method to record all orders
     */
    public void record(final Order order) {
        processedOrders.add(order);

        AtomicInteger tableCount = tableOrdersMap.getOrDefault(order.getTable(), new AtomicInteger());
        tableCount.incrementAndGet();
        tableOrdersMap.put(order.getTable(), tableCount);

        AtomicInteger waiterCount = waiterOrdersMap.getOrDefault(order.getTable().getWaiter(), new AtomicInteger());
        waiterCount.incrementAndGet();
        waiterOrdersMap.put(order.getTable().getWaiter(), waiterCount);

        AtomicInteger chefCount = chefOrdersMap.getOrDefault(order.getChef(), new AtomicInteger());
        chefCount.incrementAndGet();
        chefOrdersMap.put(order.getChef(), chefCount);
    }

    public boolean validate() {
        int[] ints = new int[3];
        getTotalOrderCounts(ints);

        int totalTableOrders = ints[0];
        int totalWaiterOrders = ints[1];
        int totalChefOrders = ints[2];

        boolean reconciled = true;
        if(totalTableOrders != processedOrders.size()) {
            reconciled = false;
            logger.debug("number of orders placed from all tables don't match with total orders processed. table-orders:{}, total-orders:{}",
                    totalTableOrders, processedOrders.size());
        }

        if(totalWaiterOrders != processedOrders.size()) {
            reconciled = false;
            logger.debug("number of orders delivered by all waiters don't match with total orders processed. waiter-orders:{}, total-orders:{}",
                    totalWaiterOrders, processedOrders.size());
        }

        if(totalChefOrders != processedOrders.size()) {
            reconciled = false;
            logger.debug("number of orders cooked by all chefs don't match with total orders processed. chef-orders:{}, total-orders:{}",
                    totalChefOrders, processedOrders.size());
        }

        if(reconciled) {
            logger.debug("Total orders processed matches with total orders from all tables, all waiters and all chefs.");
        }

        return reconciled;
    }

    public void generateReport() {
        int[] ints = new int[3];
        getTotalOrderCounts(ints);

        int totalTableOrders = ints[0];
        int totalWaiterOrders = ints[1];
        int totalChefOrders = ints[2];

        String report = reportGenerator.generateReport(processedOrders.size(), totalTableOrders, totalWaiterOrders, totalChefOrders);

        try {
            Files.write(Paths.get("report.txt"), report.getBytes());
        } catch (IOException e) {
            logger.error("Can't write the report to file.", e);
        }
    }

    private void getTotalOrderCounts(int[] ints) {
        for(Map.Entry<Table, AtomicInteger> entry: tableOrdersMap.entrySet()) {
            ints[0] += entry.getValue().get();
        }

        for(Map.Entry<Waiter, AtomicInteger> entry: waiterOrdersMap.entrySet()) {
            ints[1] += entry.getValue().get();
        }

        for(Map.Entry<Chef, AtomicInteger> entry: chefOrdersMap.entrySet()) {
            ints[2] += entry.getValue().get();
        }
    }
}
