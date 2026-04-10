package hotel_system.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "amenities")
public class Amenity implements Serializable {
    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    public Amenity() {
    }

    public Amenity(String id, String name, double price) {
        setId(id);
        setName(name);
        setPrice(price);
    }

    public Amenity(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @PrePersist
    private void generateId() {
        if (this.id == null) {
            this.id = "AM-" + System.currentTimeMillis() + "-" +
                    UUID.randomUUID().toString().substring(0, 4);
        }
    }
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Amenity name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Amenity name cannot be blank");
        }
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.price = price;
    }

    public String toString() {return String.format("Amenity[id=%s, name=%s, price=%.2f]", id, name, price);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amenity amenity = (Amenity) o;
        return Objects.equals(id, amenity.id);
    }

    public int hashCode() {
        return Objects.hash(id);
    }
}