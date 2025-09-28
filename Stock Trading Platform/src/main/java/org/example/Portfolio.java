import java.util.*;
import java.time.LocalDateTime;

public class Portfolio {
    private String userId;
    private Map<String, StockHolding> holdings;
    private double cashBalance;
    private double initialInvestment;
    private List<Transaction> transactionHistory;
    private Map<String, Double> averageCosts;

    public Portfolio(String userId, double initialCash) {
        this.userId = userId;
        this.cashBalance = initialCash;
        this.initialInvestment = initialCash;
        this.holdings = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
        this.averageCosts = new HashMap<>();
    }

    public static class StockHolding {
        private String symbol;
        private int quantity;
        private double averageCost;
        private double totalCost;

        public StockHolding(String symbol, int quantity, double averageCost) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averageCost = averageCost;
            this.totalCost = quantity * averageCost;
        }

        public void addShares(int additionalShares, double price) {
            double additionalCost = additionalShares * price;
            this.totalCost += additionalCost;
            this.quantity += additionalShares;
            this.averageCost = this.totalCost / this.quantity;
        }

        public boolean removeShares(int sharesToRemove) {
            if (sharesToRemove > quantity) {
                return false;
            }
            this.quantity -= sharesToRemove;
            if (this.quantity == 0) {
                this.totalCost = 0;
                this.averageCost = 0;
            } else {
                this.totalCost = this.quantity * this.averageCost;
            }
            return true;
        }

        public double getCurrentValue(double currentPrice) {
            return quantity * currentPrice;
        }

        public double getUnrealizedProfitLoss(double currentPrice) {
            return getCurrentValue(currentPrice) - (quantity * averageCost);
        }

        public String getSymbol() { return symbol; }
        public int getQuantity() { return quantity; }
        public double getAverageCost() { return averageCost; }
        public double getTotalCost() { return totalCost; }
    }

    public boolean buyStock(Stock stock, int quantity, double commission) {
        double totalCost = (stock.getCurrentPrice() * quantity) + commission;

        if (totalCost > cashBalance) {
            return false;
        }

        cashBalance -= totalCost;

        String symbol = stock.getSymbol();
        if (holdings.containsKey(symbol)) {
            holdings.get(symbol).addShares(quantity, stock.getCurrentPrice());
        } else {
            holdings.put(symbol, new StockHolding(symbol, quantity, stock.getCurrentPrice()));
        }

        Transaction transaction = new Transaction(userId, symbol, Transaction.TransactionType.BUY,
                quantity, stock.getCurrentPrice(), commission);
        transactionHistory.add(transaction);

        return true;
    }

    public boolean sellStock(Stock stock, int quantity, double commission) {
        String symbol = stock.getSymbol();

        if (!holdings.containsKey(symbol) || holdings.get(symbol).getQuantity() < quantity) {
            return false;
        }

        StockHolding holding = holdings.get(symbol);
        double totalRevenue = (stock.getCurrentPrice() * quantity) - commission;

        cashBalance += totalRevenue;

        holding.removeShares(quantity);
        if (holding.getQuantity() == 0) {
            holdings.remove(symbol);
        }

        Transaction transaction = new Transaction(userId, symbol, Transaction.TransactionType.SELL,
                quantity, stock.getCurrentPrice(), commission);
        transactionHistory.add(transaction);

        return true;
    }

    public double getTotalValue(Map<String, Stock> stockMap) {
        double totalStockValue = 0;

        for (StockHolding holding : holdings.values()) {
            Stock stock = stockMap.get(holding.getSymbol());
            if (stock != null) {
                totalStockValue += holding.getCurrentValue(stock.getCurrentPrice());
            }
        }

        return cashBalance + totalStockValue;
    }

    public double getUnrealizedProfitLoss(Map<String, Stock> stockMap) {
        double totalUnrealized = 0;

        for (StockHolding holding : holdings.values()) {
            Stock stock = stockMap.get(holding.getSymbol());
            if (stock != null) {
                totalUnrealized += holding.getUnrealizedProfitLoss(stock.getCurrentPrice());
            }
        }

        return totalUnrealized;
    }

    public double getRealizedProfitLoss() {
        double realizedPL = 0;
        Map<String, List<Transaction>> buysBySymbol = new HashMap<>();

        for (Transaction tx : transactionHistory) {
            if (tx.getType() == Transaction.TransactionType.BUY) {
                buysBySymbol.computeIfAbsent(tx.getStockSymbol(), k -> new ArrayList<>()).add(tx);
            }
        }

        for (Transaction tx : transactionHistory) {
            if (tx.getType() == Transaction.TransactionType.SELL) {
                List<Transaction> buys = buysBySymbol.get(tx.getStockSymbol());
                if (buys != null && !buys.isEmpty()) {
                    double avgBuyPrice = buys.stream()
                            .mapToDouble(Transaction::getPricePerShare)
                            .average()
                            .orElse(0.0);
                    realizedPL += tx.calculateProfitLoss(avgBuyPrice);
                }
            }
        }

        return realizedPL;
    }

    public double getTotalReturnPercentage(Map<String, Stock> stockMap) {
        double currentValue = getTotalValue(stockMap);
        if (initialInvestment == 0) return 0.0;
        return ((currentValue - initialInvestment) / initialInvestment) * 100;
    }

    public int getPortfolioDiversity() {
        return holdings.size();
    }

    public String getLargestHolding(Map<String, Stock> stockMap) {
        if (holdings.isEmpty()) return "None";

        String largest = "";
        double maxValue = 0;

        for (StockHolding holding : holdings.values()) {
            Stock stock = stockMap.get(holding.getSymbol());
            if (stock != null) {
                double value = holding.getCurrentValue(stock.getCurrentPrice());
                if (value > maxValue) {
                    maxValue = value;
                    largest = holding.getSymbol();
                }
            }
        }

        return largest;
    }

    public void addCash(double amount) {
        if (amount > 0) {
            cashBalance += amount;
            initialInvestment += amount; // Track additional investments
        }
    }

    public boolean withdrawCash(double amount) {
        if (amount <= cashBalance) {
            cashBalance -= amount;
            return true;
        }
        return false;
    }


    public String getPortfolioSummary(Map<String, Stock> stockMap) {
        StringBuilder summary = new StringBuilder();
        summary.append("\n").append("=".repeat(60)).append("\n");
        summary.append("PORTFOLIO SUMMARY - User: ").append(userId).append("\n");
        summary.append("=".repeat(60)).append("\n");

        double totalValue = getTotalValue(stockMap);
        double stockValue = totalValue - cashBalance;

        summary.append(String.format("Cash Balance: $%.2f\n", cashBalance));
        summary.append(String.format("Stock Holdings Value: $%.2f\n", stockValue));
        summary.append(String.format("Total Portfolio Value: $%.2f\n", totalValue));
        summary.append(String.format("Initial Investment: $%.2f\n", initialInvestment));

        double totalReturn = totalValue - initialInvestment;
        double returnPercent = getTotalReturnPercentage(stockMap);

        summary.append(String.format("Total Return: $%.2f (%.2f%%)\n", totalReturn, returnPercent));
        summary.append(String.format("Unrealized P&L: $%.2f\n", getUnrealizedProfitLoss(stockMap)));
        summary.append(String.format("Realized P&L: $%.2f\n", getRealizedProfitLoss()));

        summary.append("\nPORTFOLIO STATISTICS:\n");
        summary.append("-".repeat(30)).append("\n");
        summary.append(String.format("Number of Holdings: %d\n", getPortfolioDiversity()));
        summary.append(String.format("Largest Position: %s\n", getLargestHolding(stockMap)));
        summary.append(String.format("Total Transactions: %d\n", transactionHistory.size()));

        if (!holdings.isEmpty()) {
            summary.append("\nCURRENT HOLDINGS:\n");
            summary.append("-".repeat(80)).append("\n");
            summary.append(String.format("%-8s %-12s %-12s %-12s %-12s %-12s\n",
                    "Symbol", "Quantity", "Avg Cost", "Current", "Value", "P&L"));
            summary.append("-".repeat(80)).append("\n");

            for (StockHolding holding : holdings.values()) {
                Stock stock = stockMap.get(holding.getSymbol());
                if (stock != null) {
                    double currentValue = holding.getCurrentValue(stock.getCurrentPrice());
                    double pl = holding.getUnrealizedProfitLoss(stock.getCurrentPrice());

                    summary.append(String.format("%-8s %-12d $%-11.2f $%-11.2f $%-11.2f $%-11.2f\n",
                            holding.getSymbol(),
                            holding.getQuantity(),
                            holding.getAverageCost(),
                            stock.getCurrentPrice(),
                            currentValue,
                            pl));
                }
            }
        }

        summary.append("=".repeat(60));
        return summary.toString();
    }

    public String getUserId() { return userId; }
    public double getCashBalance() { return cashBalance; }
    public double getInitialInvestment() { return initialInvestment; }
    public Map<String, StockHolding> getHoldings() { return new HashMap<>(holdings); }
    public List<Transaction> getTransactionHistory() { return new ArrayList<>(transactionHistory); }

    public List<Transaction> getRecentTransactions() {
        List<Transaction> recent = new ArrayList<>(transactionHistory);
        recent.sort((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp())); // Sort by newest first
        return recent.subList(0, Math.min(10, recent.size()));
    }
}