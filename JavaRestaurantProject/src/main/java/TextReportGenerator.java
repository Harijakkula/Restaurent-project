public class TextReportGenerator implements ReportGenerator {
    @Override
    public String generateReport(int totalOrdersProcessed, int totalOrdersFromAllTables, int totalOrdersFromAllWaiters, int totalOrdersCooked) {
        StringBuilder builder = new StringBuilder();
        builder.append("Total orders processed: ").append(totalOrdersProcessed).append(System.lineSeparator());
        builder.append("Total orders from all tables: ").append(totalOrdersFromAllTables).append(System.lineSeparator());
        builder.append("Total orders from all waiters: ").append(totalOrdersFromAllWaiters).append(System.lineSeparator());
        builder.append("Total orders cooked: ").append(totalOrdersCooked).append(System.lineSeparator());
        return builder.toString();
    }
}
