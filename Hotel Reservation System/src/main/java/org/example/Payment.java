import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Payment {
    public enum PaymentMethod {
        CASH, CREDIT_CARD, DEBIT_CARD, PAYPAL, BANK_TRANSFER, DIGITAL_WALLET
    }

    public enum PaymentStatus {
        PENDING, PROCESSING, COMPLETED, FAILED, REFUNDED, CANCELLED
    }

    private String paymentId;
    private String reservationId;
    private double amount;
    private PaymentMethod method;
    private PaymentStatus status;
    private LocalDateTime paymentDateTime;
    private String transactionReference;
    private String cardNumber;
    private String cardHolderName;
    private String description;
    private double processingFee;

    public Payment(String reservationId, double amount, PaymentMethod method) {
        this.paymentId = generatePaymentId();
        this.reservationId = reservationId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.paymentDateTime = LocalDateTime.now();
        this.transactionReference = generateTransactionReference();
        this.description = "Hotel reservation payment";
        this.processingFee = calculateProcessingFee(amount, method);
        this.cardNumber = "";
        this.cardHolderName = "";
    }

    private String generatePaymentId() {
        return "PAY" + System.currentTimeMillis() % 1000000;
    }

    private String generateTransactionReference() {
        return "TXN" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + 
               new Random().nextInt(1000);
    }

    private double calculateProcessingFee(double amount, PaymentMethod method) {
        switch (method) {
            case CREDIT_CARD:
                return amount * 0.029;
            case DEBIT_CARD:
                return amount * 0.015;
            case PAYPAL:
                return amount * 0.034;
            case DIGITAL_WALLET:
                return amount * 0.025;
            case BANK_TRANSFER:
                return 5.0;
            case CASH:
            default:
                return 0.0;
        }
    }

    public boolean processPayment() {
        status = PaymentStatus.PROCESSING;

        boolean paymentSuccessful = simulatePaymentProcessing();

        if (paymentSuccessful) {
            status = PaymentStatus.COMPLETED;
            paymentDateTime = LocalDateTime.now();
            return true;
        } else {
            status = PaymentStatus.FAILED;
            return false;
        }
    }

    private boolean simulatePaymentProcessing() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Random random = new Random();
        int successRate = getSuccessRateByMethod();
        return random.nextInt(100) < successRate;
    }

    private int getSuccessRateByMethod() {
        switch (method) {
            case CASH:
                return 100;
            case CREDIT_CARD:
            case DEBIT_CARD:
                return 95;
            case PAYPAL:
            case DIGITAL_WALLET:
                return 92;
            case BANK_TRANSFER:
                return 98;
            default:
                return 90;
        }
    }

    public boolean refundPayment() {
        if (status == PaymentStatus.COMPLETED) {
            status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }

    public void cancelPayment() {
        if (status == PaymentStatus.PENDING || status == PaymentStatus.PROCESSING) {
            status = PaymentStatus.CANCELLED;
        }
    }

    public double getTotalAmount() {
        return amount + processingFee;
    }

    public boolean isSuccessful() {
        return status == PaymentStatus.COMPLETED;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING || status == PaymentStatus.PROCESSING;
    }

    public void setCardDetails(String cardNumber, String cardHolderName) {
        if (method == PaymentMethod.CREDIT_CARD || method == PaymentMethod.DEBIT_CARD) {
            this.cardNumber = maskCardNumber(cardNumber);
            this.cardHolderName = cardHolderName;
        }
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "**** **** **** " + lastFour;
    }

    public String getPaymentReceipt() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("PAYMENT RECEIPT\n");
        receipt.append("=".repeat(40)).append("\n");
        receipt.append("Payment ID: ").append(paymentId).append("\n");
        receipt.append("Reservation ID: ").append(reservationId).append("\n");
        receipt.append("Transaction Ref: ").append(transactionReference).append("\n");
        receipt.append("Date & Time: ").append(paymentDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        receipt.append("Amount: $").append(String.format("%.2f", amount)).append("\n");
        receipt.append("Processing Fee: $").append(String.format("%.2f", processingFee)).append("\n");
        receipt.append("Total Charged: $").append(String.format("%.2f", getTotalAmount())).append("\n");
        receipt.append("Payment Method: ").append(method).append("\n");
        if (!cardNumber.isEmpty()) {
            receipt.append("Card Number: ").append(cardNumber).append("\n");
            receipt.append("Card Holder: ").append(cardHolderName).append("\n");
        }
        receipt.append("Status: ").append(status).append("\n");
        receipt.append("Description: ").append(description).append("\n");
        receipt.append("=".repeat(40));
        return receipt.toString();
    }

    public String getPaymentSummary() {
        return String.format("Payment %s - $%.2f via %s - %s", 
                paymentId, amount, method, status);
    }

    @Override
    public String toString() {
        return String.format("Payment{id='%s', amount=%.2f, method=%s, status=%s}", 
                paymentId, amount, method, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Payment payment = (Payment) obj;
        return paymentId.equals(payment.paymentId);
    }

    @Override
    public int hashCode() {
        return paymentId.hashCode();
    }

    public String getPaymentId() { return paymentId; }
    public String getReservationId() { return reservationId; }
    public double getAmount() { return amount; }
    public PaymentMethod getMethod() { return method; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getPaymentDateTime() { return paymentDateTime; }
    public String getTransactionReference() { return transactionReference; }
    public String getCardNumber() { return cardNumber; }
    public String getCardHolderName() { return cardHolderName; }
    public String getDescription() { return description; }
    public double getProcessingFee() { return processingFee; }

    public void setDescription(String description) { this.description = description; }
    public void setAmount(double amount) { 
        this.amount = amount;
        this.processingFee = calculateProcessingFee(amount, method);
    }
}