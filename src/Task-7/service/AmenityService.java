package service;

import interfaceClass.*;
import model.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AmenityService {
    private final IAmenityRepository amenityRepository;

    public AmenityService(IAmenityRepository amenityRepository) {
        this.amenityRepository = Objects.requireNonNull(amenityRepository,
                "AmenityRepository cannot be null");
    }

    public void addAmenity(Amenity amenity) {
        amenityRepository.addAmenity(amenity);
    }

    public boolean doesAmenityExist(String amenityName) {
        return amenityRepository.containsAmenity(amenityName);
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        amenityRepository.updateAmenityPrice(amenityName, newPrice);
    }

    public List<Amenity> getAllAmenities() {
        return amenityRepository.getAllAmenities();
    }

    public List<Amenity> getAmenitiesSortedByPrice() {
        return amenityRepository.getSortedAmenities(Comparator.comparingDouble(Amenity::getPrice));
    }

    public List<Amenity> getAmenitiesSortedByName() {
        return amenityRepository.getSortedAmenities(Comparator.comparing(Amenity::getName));
    }

    public void updateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        amenityRepository.updateAmenity(amenity);
    }

    public Optional<Amenity> findAmenityByName(String name) {
        return amenityRepository.findAmenityByName(name);
    }

    public void clearAll() {
        amenityRepository.clearAll();
    }
}