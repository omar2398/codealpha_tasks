import java.time.LocalDate;

public class Room {
    public enum RoomType {
        STANDARD(100.0), DELUXE(150.0), SUITE(250.0);

        private final double basePrice;

        RoomType(double basePrice) {
            this.basePrice = basePrice;
        }

        public double getBasePrice() {
            return basePrice;
        }
    }

    public enum RoomStatus {
        AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_ORDER
    }

    private String roomNumber;
    private RoomType type;
    private RoomStatus status;
    private int capacity;
    private boolean hasWifi;
    private boolean hasAirConditioning;
    private boolean hasBalcony;
    private double pricePerNight;
    private String description;
    private int floor;

    public Room(String roomNumber, RoomType type, int capacity, int floor) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.capacity = capacity;
        this.floor = floor;
        this.status = RoomStatus.AVAILABLE;
        this.pricePerNight = type.getBasePrice();
        this.hasWifi = true;
        this.hasAirConditioning = true;
        this.hasBalcony = false;
        setDefaultDescription();
    }

    private void setDefaultDescription() {
        switch (type) {
            case STANDARD:
                this.description = "Comfortable standard room with basic amenities";
                break;
            case DELUXE:
                this.description = "Spacious deluxe room with premium amenities";
                this.hasBalcony = true;
                break;
            case SUITE:
                this.description = "Luxury suite with separate living area and premium facilities";
                this.hasBalcony = true;
                this.pricePerNight = type.getBasePrice() + 50.0;
                break;
        }
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }

    public boolean isAvailableForDates(LocalDate checkIn, LocalDate checkOut) {
        return status == RoomStatus.AVAILABLE;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void markAsOccupied() {
        this.status = RoomStatus.OCCUPIED;
    }

    public void markAsAvailable() {
        this.status = RoomStatus.AVAILABLE;
    }

    public void markForMaintenance() {
        this.status = RoomStatus.MAINTENANCE;
    }

    public double calculatePrice(int numberOfNights) {
        return pricePerNight * numberOfNights;
    }

    public String getRoomDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Room ").append(roomNumber).append(" (").append(type).append(")\n");
        details.append("Floor: ").append(floor).append("\n");
        details.append("Capacity: ").append(capacity).append(" guests\n");
        details.append("Price: $").append(String.format("%.2f", pricePerNight)).append(" per night\n");
        details.append("Status: ").append(status).append("\n");
        details.append("WiFi: ").append(hasWifi ? "Yes" : "No").append("\n");
        details.append("Air Conditioning: ").append(hasAirConditioning ? "Yes" : "No").append("\n");
        details.append("Balcony: ").append(hasBalcony ? "Yes" : "No").append("\n");
        details.append("Description: ").append(description);
        return details.toString();
    }

    @Override
    public String toString() {
        return String.format("Room %s (%s) - $%.2f/night - %s", 
                roomNumber, type, pricePerNight, status);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Room room = (Room) obj;
        return roomNumber.equals(room.roomNumber);
    }

    @Override
    public int hashCode() {
        return roomNumber.hashCode();
    }

    public String getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public RoomStatus getStatus() { return status; }
    public int getCapacity() { return capacity; }
    public boolean hasWifi() { return hasWifi; }
    public boolean hasAirConditioning() { return hasAirConditioning; }
    public boolean hasBalcony() { return hasBalcony; }
    public double getPricePerNight() { return pricePerNight; }
    public String getDescription() { return description; }
    public int getFloor() { return floor; }

    public void setHasWifi(boolean hasWifi) { this.hasWifi = hasWifi; }
    public void setHasAirConditioning(boolean hasAirConditioning) { this.hasAirConditioning = hasAirConditioning; }
    public void setHasBalcony(boolean hasBalcony) { this.hasBalcony = hasBalcony; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }
    public void setDescription(String description) { this.description = description; }
}