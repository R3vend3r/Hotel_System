package hotelsystem.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;

@Getter
public class AmenityOrder extends Order {
    @Getter
    @Setter
    private String amenityId;
    private Date serviceDate;
    @Setter
    @Getter
    private double price;
    @Setter
    private Client client;
    @Setter
    private Amenity amenity;

    public AmenityOrder() {
    }

    public AmenityOrder(String id, String clientId, double totalPrice,
                        String amenityId, Date serviceDate) {
        super(id, clientId, totalPrice, new Date(), serviceDate);
        setAmenityId(amenityId);
        setServiceDate(serviceDate);
        setPrice(totalPrice);
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