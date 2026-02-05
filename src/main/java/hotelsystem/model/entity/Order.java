package hotelsystem.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@MappedSuperclass
public abstract class Order implements Serializable {
    @Id
    private String id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "check_in_date")
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Transient
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
        this.checkInDate = Objects.requireNonNull(creationDate, "Creation date cannot be null");
    }

    public void setAvailableDate(Date checkOutDate) {
        if (checkOutDate == null) {
            throw new IllegalArgumentException("Available date cannot be null");
        }
        this.checkOutDate = checkOutDate;
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