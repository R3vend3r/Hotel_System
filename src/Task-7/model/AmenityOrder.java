package model;

import java.util.Date;
import java.util.Objects;

public class AmenityOrder extends Order {
    private Amenity amenity;
    private Date serviceDate;

    public AmenityOrder() {
    }

    public AmenityOrder(String id, Client client, double totalPrice, Amenity amenity, Date serviceDate) {
        super(id, client, totalPrice, new Date(), serviceDate);
        setAmenity(amenity);
        setServiceDate(serviceDate);
        setTotalPrice(amenity.getPrice());
    }

    public AmenityOrder(Client client, Amenity amenity, Date serviceDate) {
        super(client, amenity.getPrice(), new Date(), serviceDate);
        setAmenity(amenity);
        setServiceDate(serviceDate);
    }

    public Amenity getAmenity() { return amenity; }
    public Date getServiceDate() { return serviceDate; }
    public void setAmenity(Amenity amenity) {
        this.amenity = Objects.requireNonNull(amenity, "Amenity cannot be null");
    }
    public void setServiceDate(Date serviceDate) {
        this.serviceDate = Objects.requireNonNull(serviceDate, "Service date cannot be null");
        setAvailableDate(serviceDate);
    }
}