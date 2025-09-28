import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Reservation {
    public enum ReservationStatus {
        PENDING, CONFIRMED, CHECKED_IN, CHECKED_OUT, CANCELLED, NO_SHOW
    }

    private String reservationId;
    private String guestId;
    private String roomNumber;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private int numberOfGuests;
    private ReservationStatus status;
    private double totalAmount;
    private double amountPaid;
    private LocalDateTime bookingDateTime;
    private String specialRequests;
    private boolean isPaid;
    private String paymentMethod;
    private double discountAmount;
    private String discountReason;

    public Reservation(String guestId, String roomNumber, LocalDate checkInDate, 
                      LocalDate checkOutDate, int numberOfGuests, double totalAmount) {
        this.reservationId = generateReservationId();
        this.guestId = guestId;
        this.roomNumber = roomNumber;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.numberOfGuests = numberOfGuests;
        this.totalAmount = totalAmount;
        this.status = ReservationStatus.PENDING;
        this.bookingDateTime = LocalDateTime.now();
        this.amountPaid = 0.0;
        this.isPaid = false;
        this.specialRequests = "";
        this.paymentMethod = "";
        this.discountAmount = 0.0;
        this.discountReason = "";
    }

    private String generateReservationId() {
        return "RES" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public int getNumberOfNights() {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public void confirmReservation() {
        if (status == ReservationStatus.PENDING) {
            status = ReservationStatus.CONFIRMED;
        }
    }

    public void cancelReservation() {
        if (status != ReservationStatus.CHECKED_IN && status != ReservationStatus.CHECKED_OUT) {
            status = ReservationStatus.CANCELLED;
        }
    }

    public void checkIn() {
        if (status == ReservationStatus.CONFIRMED) {
            status = ReservationStatus.CHECKED_IN;
        }
    }

    public void checkOut() {
        if (status == ReservationStatus.CHECKED_IN) {
            status = ReservationStatus.CHECKED_OUT;
        }
    }

    public void markAsNoShow() {
        if (status == ReservationStatus.CONFIRMED && 
            LocalDate.now().isAfter(checkInDate)) {
            status = ReservationStatus.NO_SHOW;
        }
    }

    public boolean makePayment(double amount, String paymentMethod) {
        if (amount > 0 && amount <= getRemainingAmount()) {
            this.amountPaid += amount;
            this.paymentMethod = paymentMethod;

            if (amountPaid >= totalAmount) {
                this.isPaid = true;
                if (status == ReservationStatus.PENDING) {
                    confirmReservation();
                }
            }
            return true;
        }
        return false;
    }

    public double getRemainingAmount() {
        return Math.max(0, totalAmount - amountPaid);
    }

    public void applyDiscount(double discountAmount, String reason) {
        this.discountAmount = discountAmount;
        this.discountReason = reason;
        this.totalAmount = Math.max(0, this.totalAmount - discountAmount);
    }

    public boolean canBeCancelled() {
        return status != ReservationStatus.CHECKED_IN && 
               status != ReservationStatus.CHECKED_OUT && 
               status != ReservationStatus.CANCELLED;
    }

    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED || status == ReservationStatus.CHECKED_IN;
    }

    public boolean isUpcoming() {
        return status == ReservationStatus.CONFIRMED && 
               checkInDate.isAfter(LocalDate.now());
    }

    public boolean isCurrent() {
        LocalDate today = LocalDate.now();
        return status == ReservationStatus.CHECKED_IN ||
               (status == ReservationStatus.CONFIRMED && 
                !checkInDate.isAfter(today) && checkOutDate.isAfter(today));
    }

    public String getReservationSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Reservation ID: ").append(reservationId).append("\n");
        summary.append("Guest ID: ").append(guestId).append("\n");
        summary.append("Room: ").append(roomNumber).append("\n");
        summary.append("Check-in: ").append(checkInDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        summary.append("Check-out: ").append(checkOutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n");
        summary.append("Nights: ").append(getNumberOfNights()).append("\n");
        summary.append("Guests: ").append(numberOfGuests).append("\n");
        summary.append("Status: ").append(status).append("\n");
        summary.append("Total Amount: $").append(String.format("%.2f", totalAmount)).append("\n");
        summary.append("Amount Paid: $").append(String.format("%.2f", amountPaid)).append("\n");
        summary.append("Remaining: $").append(String.format("%.2f", getRemainingAmount())).append("\n");
        summary.append("Payment Status: ").append(isPaid ? "Paid" : "Pending").append("\n");
        if (!paymentMethod.isEmpty()) {
            summary.append("Payment Method: ").append(paymentMethod).append("\n");
        }
        if (discountAmount > 0) {
            summary.append("Discount: $").append(String.format("%.2f", discountAmount));
            summary.append(" (").append(discountReason).append(")\n");
        }
        if (!specialRequests.isEmpty()) {
            summary.append("Special Requests: ").append(specialRequests).append("\n");
        }
        summary.append("Booking Date: ").append(bookingDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return summary.toString();
    }

    @Override
    public String toString() {
        return String.format("Reservation{id='%s', room='%s', dates='%s to %s', status=%s}", 
                reservationId, roomNumber, checkInDate, checkOutDate, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Reservation that = (Reservation) obj;
        return reservationId.equals(that.reservationId);
    }

    @Override
    public int hashCode() {
        return reservationId.hashCode();
    }

    public String getReservationId() { return reservationId; }
    public String getGuestId() { return guestId; }
    public String getRoomNumber() { return roomNumber; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public int getNumberOfGuests() { return numberOfGuests; }
    public ReservationStatus getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public double getAmountPaid() { return amountPaid; }
    public LocalDateTime getBookingDateTime() { return bookingDateTime; }
    public String getSpecialRequests() { return specialRequests; }
    public boolean isPaid() { return isPaid; }
    public String getPaymentMethod() { return paymentMethod; }
    public double getDiscountAmount() { return discountAmount; }
    public String getDiscountReason() { return discountReason; }

    public void setStatus(ReservationStatus status) { this.status = status; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public void setNumberOfGuests(int numberOfGuests) { this.numberOfGuests = numberOfGuests; }
}