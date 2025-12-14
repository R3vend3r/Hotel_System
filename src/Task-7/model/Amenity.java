package model;

import java.io.Serializable;
import java.util.Objects;

public class Amenity implements Serializable {
    private String id;
    private String name;
    private double price;

    public Amenity() {
    }

    public Amenity(String id, String name, double price) {
        setId(id);
        setName(name);
        setPrice(price);
    }

    public Amenity(String name, double price) {
        this(generateId(), name, price);
    }

    private static String generateId() {
        return "AM-" + System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Amenity name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Amenity name cannot be blank");
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = price;
    }

    public String toString() {return String.format("Amenity[id=%s, name=%s, price=%.2f]", id, name, price);}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amenity amenity = (Amenity) o;
        return id.equals(amenity.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }
}