public interface ReportGenerator {
    String generateReport(int totalOrdersProcessed, int totalOrdersFromAllTables, int totalOrdersFromAllWaiters, int totalOrdersCooked);
}
