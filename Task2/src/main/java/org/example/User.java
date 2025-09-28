import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class User {
    private String userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
    private UserStatus status;
    private RiskProfile riskProfile;
    private Portfolio portfolio;
    private TradingPreferences preferences;
    private List<String> watchlist;
    private boolean isActive;

    public enum UserStatus {
        ACTIVE, INACTIVE, SUSPENDED, PENDING_VERIFICATION
    }

    public enum RiskProfile {
        CONSERVATIVE, MODERATE, AGGRESSIVE, VERY_AGGRESSIVE
    }

    public static class TradingPreferences {
        private boolean enableNotifications;
        private double maxDailyLoss;
        private double maxPositionSize;
        private boolean allowMarginTrading;
        private boolean allowAfterHoursTrading;
        private String preferredOrderType; // MARKET, LIMIT, STOP_LOSS

        public TradingPreferences() {
            this.enableNotifications = true;
            this.maxDailyLoss = 1000.0;
            this.maxPositionSize = 0.1;
            this.allowMarginTrading = false;
            this.allowAfterHoursTrading = false;
            this.preferredOrderType = "MARKET";
        }

        public boolean isEnableNotifications() { return enableNotifications; }
        public void setEnableNotifications(boolean enableNotifications) { 
            this.enableNotifications = enableNotifications; 
        }

        public double getMaxDailyLoss() { return maxDailyLoss; }
        public void setMaxDailyLoss(double maxDailyLoss) { 
            this.maxDailyLoss = maxDailyLoss; 
        }

        public double getMaxPositionSize() { return maxPositionSize; }
        public void setMaxPositionSize(double maxPositionSize) { 
            this.maxPositionSize = maxPositionSize; 
        }

        public boolean isAllowMarginTrading() { return allowMarginTrading; }
        public void setAllowMarginTrading(boolean allowMarginTrading) { 
            this.allowMarginTrading = allowMarginTrading; 
        }

        public boolean isAllowAfterHoursTrading() { return allowAfterHoursTrading; }
        public void setAllowAfterHoursTrading(boolean allowAfterHoursTrading) { 
            this.allowAfterHoursTrading = allowAfterHoursTrading; 
        }

        public String getPreferredOrderType() { return preferredOrderType; }
        public void setPreferredOrderType(String preferredOrderType) { 
            this.preferredOrderType = preferredOrderType; 
        }
    }

    public User(String username, String password, String firstName, String lastName, String email) {
        this.userId = generateUserId();
        this.username = username;
        this.password = password; // In production, this should be hashed
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
        this.lastLoginDate = LocalDateTime.now();
        this.status = UserStatus.ACTIVE;
        this.riskProfile = RiskProfile.MODERATE; // Default risk profile
        this.portfolio = new Portfolio(userId, 10000.0); // Start with $10,000
        this.preferences = new TradingPreferences();
        this.watchlist = new ArrayList<>();
        this.isActive = true;
        this.phoneNumber = "";
    }

    private String generateUserId() {
        return "USER" + System.currentTimeMillis() % 1000000;
    }

    public boolean authenticate(String inputPassword) {
        if (this.password.equals(inputPassword) && isActive) {
            this.lastLoginDate = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public boolean updatePassword(String oldPassword, String newPassword) {
        if (authenticate(oldPassword)) {
            this.password = newPassword;
            return true;
        }
        return false;
    }

    public boolean addToWatchlist(String stockSymbol) {
        String symbol = stockSymbol.toUpperCase();
        if (!watchlist.contains(symbol)) {
            watchlist.add(symbol);
            return true;
        }
        return false;
    }

    public boolean removeFromWatchlist(String stockSymbol) {
        return watchlist.remove(stockSymbol.toUpperCase());
    }

    public boolean canPlaceTrade(double tradeAmount, double portfolioValue) {
        double positionPercent = tradeAmount / portfolioValue;
        if (positionPercent > preferences.getMaxPositionSize()) {
            return false;
        }

        switch (riskProfile) {
            case CONSERVATIVE:
                return positionPercent <= 0.05; // Max 5% position
            case MODERATE:
                return positionPercent <= 0.10; // Max 10% position
            case AGGRESSIVE:
                return positionPercent <= 0.20; // Max 20% position
            case VERY_AGGRESSIVE:
                return positionPercent <= 0.30; // Max 30% position
            default:
                return true;
        }
    }

    public String getTradingActivityLevel() {
        int transactionCount = portfolio.getTransactionHistory().size();

        if (transactionCount == 0) return "New Trader";
        else if (transactionCount < 10) return "Beginner";
        else if (transactionCount < 50) return "Intermediate";
        else if (transactionCount < 200) return "Active";
        else return "Expert";
    }

    public double getSuccessRate() {
        List<Transaction> transactions = portfolio.getTransactionHistory();
        if (transactions.isEmpty()) return 0.0;

        int profitableTrades = 0;
        int totalTrades = 0;

        for (Transaction tx : transactions) {
            if (tx.getType() == Transaction.TransactionType.SELL) {
                totalTrades++;
                if (tx.getTotalAmount() > 0) {
                    profitableTrades++;
                }
            }
        }

        return totalTrades > 0 ? (double) profitableTrades / totalTrades * 100 : 0.0;
    }

    public String getProfileSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("\n").append("=".repeat(50)).append("\n");
        summary.append("USER PROFILE\n");
        summary.append("=".repeat(50)).append("\n");
        summary.append(String.format("User ID: %s\n", userId));
        summary.append(String.format("Name: %s %s\n", firstName, lastName));
        summary.append(String.format("Username: %s\n", username));
        summary.append(String.format("Email: %s\n", email));
        summary.append(String.format("Phone: %s\n", phoneNumber.isEmpty() ? "Not provided" : phoneNumber));
        summary.append(String.format("Registration Date: %s\n", 
                registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
        summary.append(String.format("Last Login: %s\n", 
                lastLoginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
        summary.append(String.format("Status: %s\n", status));
        summary.append(String.format("Risk Profile: %s\n", riskProfile));
        summary.append(String.format("Activity Level: %s\n", getTradingActivityLevel()));
        summary.append(String.format("Success Rate: %.1f%%\n", getSuccessRate()));

        summary.append("\nTRADING PREFERENCES:\n");
        summary.append("-".repeat(30)).append("\n");
        summary.append(String.format("Notifications: %s\n", 
                preferences.isEnableNotifications() ? "Enabled" : "Disabled"));
        summary.append(String.format("Max Daily Loss: $%.2f\n", preferences.getMaxDailyLoss()));
        summary.append(String.format("Max Position Size: %.1f%%\n", preferences.getMaxPositionSize() * 100));
        summary.append(String.format("Margin Trading: %s\n", 
                preferences.isAllowMarginTrading() ? "Allowed" : "Not Allowed"));
        summary.append(String.format("After Hours Trading: %s\n", 
                preferences.isAllowAfterHoursTrading() ? "Allowed" : "Not Allowed"));
        summary.append(String.format("Preferred Order Type: %s\n", preferences.getPreferredOrderType()));

        summary.append("\nWATCHLIST:\n");
        summary.append("-".repeat(20)).append("\n");
        if (watchlist.isEmpty()) {
            summary.append("No stocks in watchlist\n");
        } else {
            for (String symbol : watchlist) {
                summary.append(String.format("- %s\n", symbol));
            }
        }

        summary.append("=".repeat(50));
        return summary.toString();
    }

    public void deactivateAccount() {
        this.isActive = false;
        this.status = UserStatus.INACTIVE;
    }

    public void reactivateAccount() {
        this.isActive = true;
        this.status = UserStatus.ACTIVE;
    }

    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() { return firstName + " " + lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public LocalDateTime getLastLoginDate() { return lastLoginDate; }
    public UserStatus getStatus() { return status; }
    public RiskProfile getRiskProfile() { return riskProfile; }
    public Portfolio getPortfolio() { return portfolio; }
    public TradingPreferences getPreferences() { return preferences; }
    public List<String> getWatchlist() { return new ArrayList<>(watchlist); }
    public boolean isActive() { return isActive; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setStatus(UserStatus status) { this.status = status; }
    public void setRiskProfile(RiskProfile riskProfile) { this.riskProfile = riskProfile; }

    @Override
    public String toString() {
        return String.format("User{id='%s', username='%s', name='%s %s', status=%s}",
                userId, username, firstName, lastName, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}