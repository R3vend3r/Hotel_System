package interfaceClass;

import model.Amenity;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public interface IAmenityRepository {
    void addAmenity(Amenity amenity);
    boolean containsAmenity(String amenityName);
    void updateAmenityPrice(String amenityName, double newPrice);
    List<Amenity> getAllAmenities();
    List<Amenity> getSortedAmenities(Comparator<Amenity> comparator);
    Optional<Amenity> findAmenityByName(String name);

    void updateAmenity(Amenity amenity);

    void deleteAmenity(String amenityName);
    void clearAll();
}