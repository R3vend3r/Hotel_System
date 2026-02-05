package hotelsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Getter
public abstract class Order implements Serializable {
    private String id;
    @Setter
    @Getter
    private String clientId;
    private Date creationDate;
    private Date availableDate;
    private double totalPrice;
    @Setter
    private String amenityId;

    public Order() {
    }

    protected Order(String id, String clientId, double totalPrice, Date creationDate, Date availableDate) {
        setId(id);
        setClientId(clientId);
        setCreationDate(creationDate);
        setAvailableDate(availableDate);
        setTotalPrice(totalPrice);
    }

    protected Order(String id, String clientId, double totalPrice, String amenityId, Date availableDate) {
        setId(id);
        setClientId(clientId);
        setCreationDate(availableDate);
        setAmenityId(amenityId);
        setTotalPrice(totalPrice);
    }

    protected Order(String clientId, double totalPrice, Date createInDate, Date availableDate) {
        this(generateId(), clientId, totalPrice, createInDate, availableDate);
    }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = Objects.requireNonNull(creationDate, "Creation date cannot be null");
    }

    public void setAvailableDate(Date availableDate) {
        if (availableDate == null) {
            throw new IllegalArgumentException("Available date cannot be null");
        }
        this.availableDate = availableDate;
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = totalPrice;
    }
    static String generateId() {
        return "OR-" + System.currentTimeMillis();
    }

}