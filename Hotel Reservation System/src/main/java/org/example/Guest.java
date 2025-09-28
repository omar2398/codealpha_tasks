import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Guest {
    private String guestId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private String idType;
    private String idNumber;
    private String nationality;
    private List<String> reservationIds;
    private boolean isVip;
    private double loyaltyPoints;
    private String preferredRoomType;

    public Guest(String firstName, String lastName, String email, String phoneNumber) {
        this.guestId = generateGuestId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.reservationIds = new ArrayList<>();
        this.isVip = false;
        this.loyaltyPoints = 0.0;
        this.preferredRoomType = "STANDARD";
        this.nationality = "";
        this.address = "";
        this.idType = "";
        this.idNumber = "";
    }

    private String generateGuestId() {
        return "GUEST" + System.currentTimeMillis() % 100000;
    }

    public void addReservation(String reservationId) {
        if (!reservationIds.contains(reservationId)) {
            reservationIds.add(reservationId);
        }
    }

    public void removeReservation(String reservationId) {
        reservationIds.remove(reservationId);
    }

    public void addLoyaltyPoints(double points) {
        this.loyaltyPoints += points;
        checkVipStatus();
    }

    private void checkVipStatus() {
        if (loyaltyPoints >= 1000 && !isVip) {
            this.isVip = true;
        }
    }

    public double getVipDiscount() {
        return isVip ? 0.1 : 0.0;
    }

    public boolean hasValidContactInfo() {
        return email != null && !email.isEmpty() && 
               phoneNumber != null && !phoneNumber.isEmpty();
    }

    public boolean hasValidIdInfo() {
        return idType != null && !idType.isEmpty() && 
               idNumber != null && !idNumber.isEmpty();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public int getAge() {
        if (dateOfBirth == null) return 0;
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public String getGuestProfile() {
        StringBuilder profile = new StringBuilder();
        profile.append("Guest ID: ").append(guestId).append("\n");
        profile.append("Name: ").append(getFullName()).append("\n");
        profile.append("Email: ").append(email).append("\n");
        profile.append("Phone: ").append(phoneNumber).append("\n");
        profile.append("Address: ").append(address.isEmpty() ? "Not provided" : address).append("\n");
        profile.append("Nationality: ").append(nationality.isEmpty() ? "Not provided" : nationality).append("\n");
        profile.append("VIP Status: ").append(isVip ? "Yes" : "No").append("\n");
        profile.append("Loyalty Points: ").append(String.format("%.0f", loyaltyPoints)).append("\n");
        profile.append("Total Reservations: ").append(reservationIds.size()).append("\n");
        profile.append("Preferred Room Type: ").append(preferredRoomType);
        return profile.toString();
    }

    @Override
    public String toString() {
        return String.format("Guest{id='%s', name='%s', email='%s', vip=%s}", 
                guestId, getFullName(), email, isVip);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Guest guest = (Guest) obj;
        return guestId.equals(guest.guestId);
    }

    @Override
    public int hashCode() {
        return guestId.hashCode();
    }

    public String getGuestId() { return guestId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getIdType() { return idType; }
    public String getIdNumber() { return idNumber; }
    public String getNationality() { return nationality; }
    public List<String> getReservationIds() { return new ArrayList<>(reservationIds); }
    public boolean isVip() { return isVip; }
    public double getLoyaltyPoints() { return loyaltyPoints; }
    public String getPreferredRoomType() { return preferredRoomType; }

    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAddress(String address) { this.address = address; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setIdType(String idType) { this.idType = idType; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public void setVip(boolean vip) { this.isVip = vip; }
    public void setLoyaltyPoints(double loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
    public void setPreferredRoomType(String preferredRoomType) { this.preferredRoomType = preferredRoomType; }
}