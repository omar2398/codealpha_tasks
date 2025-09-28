import java.text.DecimalFormat;
import java.util.Random;

public class Stock {
    private String symbol;
    private String companyName;
    private double currentPrice;
    private double openPrice;
    private double dayHigh;
    private double dayLow;
    private long volume;
    private double previousClose;
    private String sector;
    private Random random;
    private static final DecimalFormat df = new DecimalFormat("#.##");

    public Stock(String symbol, String companyName, double initialPrice, String sector) {
        this.symbol = symbol.toUpperCase();
        this.companyName = companyName;
        this.currentPrice = initialPrice;
        this.openPrice = initialPrice;
        this.dayHigh = initialPrice;
        this.dayLow = initialPrice;
        this.previousClose = initialPrice;
        this.sector = sector;
        this.volume = 0;
        this.random = new Random();
    }

    public void simulatePriceMovement() {
        double volatility = 0.02; // 2% volatility
        double trend = (random.nextGaussian() * volatility);

        if (sector.equals("Technology")) {
            trend += 0.001;
        } else if (sector.equals("Energy")) {
            trend -= 0.0005;
        }

        double newPrice = currentPrice * (1 + trend);

        if (newPrice <= 0) {
            newPrice = currentPrice * 0.99;
        }

        updatePrice(newPrice);

        this.volume += random.nextInt(10000) + 1000;
    }


    public void updatePrice(double newPrice) {
        this.currentPrice = Math.round(newPrice * 100.0) / 100.0;

        if (this.currentPrice > this.dayHigh) {
            this.dayHigh = this.currentPrice;
        }
        if (this.currentPrice < this.dayLow) {
            this.dayLow = this.currentPrice;
        }
    }

    public double getPercentageChange() {
        if (previousClose == 0) return 0.0;
        return ((currentPrice - previousClose) / previousClose) * 100;
    }

    public String getPriceChangeString() {
        double change = currentPrice - previousClose;
        double percentChange = getPercentageChange();
        String arrow = change >= 0 ? "↑" : "↓";
        return String.format("%s $%.2f (%.2f%%)", arrow, Math.abs(change), Math.abs(percentChange));
    }

    public void resetDailyStats() {
        this.previousClose = this.currentPrice;
        this.openPrice = this.currentPrice;
        this.dayHigh = this.currentPrice;
        this.dayLow = this.currentPrice;
        this.volume = 0;
    }

    public double getMarketCap() {
        return currentPrice * 1000000000;
    }

    public String getSymbol() { return symbol; }
    public String getCompanyName() { return companyName; }
    public double getCurrentPrice() { return currentPrice; }
    public double getOpenPrice() { return openPrice; }
    public double getDayHigh() { return dayHigh; }
    public double getDayLow() { return dayLow; }
    public long getVolume() { return volume; }
    public double getPreviousClose() { return previousClose; }
    public String getSector() { return sector; }

    public void setCurrentPrice(double currentPrice) { 
        updatePrice(currentPrice); 
    }

    public void setPreviousClose(double previousClose) { 
        this.previousClose = previousClose; 
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - $%.2f %s", 
                companyName, symbol, currentPrice, getPriceChangeString());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Stock stock = (Stock) obj;
        return symbol.equals(stock.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }

    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append(String.format("Symbol: %s\n", symbol));
        info.append(String.format("Company: %s\n", companyName));
        info.append(String.format("Sector: %s\n", sector));
        info.append(String.format("Current Price: $%.2f\n", currentPrice));
        info.append(String.format("Open: $%.2f\n", openPrice));
        info.append(String.format("Day High: $%.2f\n", dayHigh));
        info.append(String.format("Day Low: $%.2f\n", dayLow));
        info.append(String.format("Previous Close: $%.2f\n", previousClose));
        info.append(String.format("Volume: %,d\n", volume));
        info.append(String.format("Change: %s\n", getPriceChangeString()));
        info.append(String.format("Market Cap: $%.2fB", getMarketCap() / 1000000000));
        return info.toString();
    }
}