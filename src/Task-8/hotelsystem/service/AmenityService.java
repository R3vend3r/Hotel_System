package hotelsystem.service;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;
import hotelsystem.interfaceClass.IAmenityRepository;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.model.Amenity;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AmenityService implements IClearable {

    @Inject
    private IAmenityRepository amenityRepository;

    @PostConstruct
    public void postConstruct() {
        System.out.println("AmenityService initialized with repository: "
                + amenityRepository.getClass().getSimpleName());
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

    @Override
    public void clear() {
        amenityRepository.clearAll();
    }
}