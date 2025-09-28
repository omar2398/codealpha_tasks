import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    public enum TransactionType {
        BUY, SELL, DIVIDEND, FEE
    }

    private String transactionId;
    private String userId;
    private String stockSymbol;
    private TransactionType type;
    private int quantity;
    private double pricePerShare;
    private double totalAmount;
    private double commission;
    private LocalDateTime timestamp;
    private String notes;

    private static int transactionCounter = 1000;

    public Transaction(String userId, String stockSymbol, TransactionType type, 
                      int quantity, double pricePerShare, double commission) {
        this.transactionId = "TXN" + String.format("%06d", ++transactionCounter);
        this.userId = userId;
        this.stockSymbol = stockSymbol.toUpperCase();
        this.type = type;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.commission = commission;
        this.timestamp = LocalDateTime.now();

        if (type == TransactionType.BUY) {
            this.totalAmount = (quantity * pricePerShare) + commission;
        } else if (type == TransactionType.SELL) {
            this.totalAmount = (quantity * pricePerShare) - commission;
        } else {
            this.totalAmount = quantity * pricePerShare;
        }

        this.notes = "";
    }

    public Transaction(String userId, String stockSymbol, double dividendAmount, String notes) {
        this.transactionId = "DIV" + String.format("%06d", ++transactionCounter);
        this.userId = userId;
        this.stockSymbol = stockSymbol.toUpperCase();
        this.type = TransactionType.DIVIDEND;
        this.quantity = 0;
        this.pricePerShare = 0;
        this.totalAmount = dividendAmount;
        this.commission = 0;
        this.timestamp = LocalDateTime.now();
        this.notes = notes != null ? notes : "Dividend payment";
    }

    public double getCashFlow() {
        switch (type) {
            case BUY:
                return -totalAmount;
            case SELL:
                return totalAmount;
            case DIVIDEND:
                return totalAmount;
            case FEE:
                return -totalAmount;
            default:
                return 0;
        }
    }


    public String getFormattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    public String getTransactionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("ID: %s | %s | %s", 
                transactionId, type.toString(), getFormattedTimestamp()));

        if (type == TransactionType.BUY || type == TransactionType.SELL) {
            summary.append(String.format(" | %s x%d @ $%.2f", 
                    stockSymbol, quantity, pricePerShare));
            if (commission > 0) {
                summary.append(String.format(" (Fee: $%.2f)", commission));
            }
            summary.append(String.format(" | Total: $%.2f", Math.abs(totalAmount)));
        } else if (type == TransactionType.DIVIDEND) {
            summary.append(String.format(" | %s | Amount: $%.2f", stockSymbol, totalAmount));
        }

        return summary.toString();
    }

    public double calculateProfitLoss(double originalBuyPrice) {
        if (type != TransactionType.SELL) {
            return 0.0;
        }

        double buyTotal = quantity * originalBuyPrice;
        double sellTotal = quantity * pricePerShare;
        return sellTotal - buyTotal - commission;
    }

    public boolean isMatchingStock(String symbol) {
        return this.stockSymbol.equals(symbol.toUpperCase());
    }

    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getStockSymbol() { return stockSymbol; }
    public TransactionType getType() { return type; }
    public int getQuantity() { return quantity; }
    public double getPricePerShare() { return pricePerShare; }
    public double getTotalAmount() { return totalAmount; }
    public double getCommission() { return commission; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return getTransactionSummary();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId.hashCode();
    }

    public String getDetailedReport() {
        StringBuilder report = new StringBuilder();
        report.append("=".repeat(50)).append("\n");
        report.append("TRANSACTION DETAILS\n");
        report.append("=".repeat(50)).append("\n");
        report.append(String.format("Transaction ID: %s\n", transactionId));
        report.append(String.format("User ID: %s\n", userId));
        report.append(String.format("Type: %s\n", type.toString()));
        report.append(String.format("Stock Symbol: %s\n", stockSymbol));
        report.append(String.format("Timestamp: %s\n", getFormattedTimestamp()));

        if (type == TransactionType.BUY || type == TransactionType.SELL) {
            report.append(String.format("Quantity: %d shares\n", quantity));
            report.append(String.format("Price per Share: $%.2f\n", pricePerShare));
            report.append(String.format("Subtotal: $%.2f\n", quantity * pricePerShare));
            report.append(String.format("Commission: $%.2f\n", commission));
        }

        report.append(String.format("Total Amount: $%.2f\n", totalAmount));
        report.append(String.format("Cash Flow Impact: %s$%.2f\n", 
                getCashFlow() >= 0 ? "+" : "", getCashFlow()));

        if (!notes.isEmpty()) {
            report.append(String.format("Notes: %s\n", notes));
        }

        report.append("=".repeat(50));
        return report.toString();
    }
}