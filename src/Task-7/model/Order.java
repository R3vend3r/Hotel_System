package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public abstract class Order implements Serializable {
    private String id;
    private Client client;
    private Date creationDate;
    private Date availableDate;
    private double totalPrice;

    public Order() {
    }

    protected Order(String id, Client client, double totalPrice, Date creationDate, Date availableDate) {
        setId(id);
        setClient(client);
        setCreationDate(creationDate);
        setAvailableDate(availableDate);
        setTotalPrice(totalPrice);
    }

    protected Order(Client client, double totalPrice,Date checkInDate, Date availableDate) {
        this(generateId(), client, totalPrice, checkInDate, availableDate);
    }

    static String generateId() {
        return "OR-" + System.currentTimeMillis();
    }

    public String getId() { return id; }
    public Client getClient() { return client; }
    public Date getCreationDate() { return creationDate; }
    public Date getAvailableDate() { return availableDate; }
    public double getTotalPrice() { return totalPrice; }

    public void setId(String id) {
        this.id = Objects.requireNonNull(id, "Order ID cannot be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("Order ID cannot be blank");
        }    }

    public void setClient(Client client) {
        this.client = Objects.requireNonNull(client, "Client cannot be null");
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = Objects.requireNonNull(creationDate, "Creation date cannot be null");
    }

    public void setAvailableDate(Date availableDate) {
        this.availableDate = Objects.requireNonNull(availableDate, "Available date cannot be null");
        if (availableDate.before(creationDate)) {
            throw new IllegalArgumentException("Available date cannot be before creation date");
        }
    }

    public void setTotalPrice(double totalPrice) {
        if (totalPrice < 0) {
            throw new IllegalArgumentException("Total price cannot be negative");
        }
        this.totalPrice = totalPrice;
    }}