public class HtmlReportGenerator implements ReportGenerator {
    @Override
    public String generateReport(int totalOrdersProcessed, int totalOrdersFromAllTables, int totalOrdersFromAllWaiters, int totalOrdersCooked) {
        StringBuilder builder = new StringBuilder();
        builder.append("<HTML> <BODY> ");
        builder.append("<p>");

        builder.append("<span>");
        builder.append("Total orders processed: ").append(totalOrdersProcessed).append(System.lineSeparator());
        builder.append("</span> <br/>");

        builder.append("<span>");
        builder.append("Total orders from all tables: ").append(totalOrdersFromAllTables).append(System.lineSeparator());
        builder.append("</span> <br/>");

        builder.append("<span>");
        builder.append("Total orders from all waiters: ").append(totalOrdersFromAllWaiters).append(System.lineSeparator());
        builder.append("</span> <br/>");

        builder.append("<span>");
        builder.append("Total orders cooked: ").append(totalOrdersCooked).append(System.lineSeparator());
        builder.append("</span> <br/>");

        builder.append("</p>");
        builder.append(" </BODY> </HTML>");

        return builder.toString();
    }
}
