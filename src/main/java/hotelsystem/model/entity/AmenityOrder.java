package hotelsystem.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "amenity_orders")
public class AmenityOrder extends Order {

    @Column(name = "amenity_id")
    private String amenityId;

    @Column(name = "service_date")
    private Date serviceDate;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "amenity_id", insertable = false, updatable = false)
    private Amenity amenity;

    public AmenityOrder() {
    }

    public AmenityOrder(String id, String clientId, double totalPrice,
                        String amenityId, Date serviceDate) {
        super(id, clientId, totalPrice, new Date(), serviceDate);
        setAmenityId(amenityId);
        setServiceDate(serviceDate);
    }

    public AmenityOrder(String id, Client client, double totalPrice, Amenity amenity, Date serviceDate ){
        super(id, client.getId(), totalPrice, amenity.getId(), serviceDate);
        setClient(client);
        setAmenity(amenity);
    }

    public void setServiceDate(Date serviceDate) {
        this.serviceDate = Objects.requireNonNull(serviceDate, "Service date cannot be null");
        setAvailableDate(serviceDate);
    }
}