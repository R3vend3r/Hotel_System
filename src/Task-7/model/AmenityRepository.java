package model;


import interfaceClass.IAmenityRepository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class AmenityRepository implements IAmenityRepository {
    private final List<Amenity> amenities;

    public AmenityRepository() {
        this.amenities = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addAmenity(Amenity amenity) throws IllegalArgumentException {
        validateAmenity(amenity);
        checkAmenityDoesNotExist(amenity.getName());
        amenities.add(amenity);
    }

    private void validateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
    }

    private void checkAmenityDoesNotExist(String amenityName) {
        if (containsAmenity(amenityName)) {
            throw new IllegalArgumentException("Amenity '" + amenityName + "' already exists");
        }
    }

    @Override
    public boolean containsAmenity(String amenityName) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        return amenities.stream()
                .anyMatch(a -> a.getName().equalsIgnoreCase(amenityName));
    }

    @Override
    public void updateAmenityPrice(String amenityName, double newPrice) throws IllegalArgumentException {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");

        Amenity amenity = findAmenityByName(amenityName)
                .orElseThrow(() -> new IllegalArgumentException("Amenity not found"));
        amenity.setPrice(newPrice);
    }

    @Override
    public List<Amenity> getAllAmenities() {
        return Collections.unmodifiableList(amenities);
    }

    @Override
    public List<Amenity> getSortedAmenities(Comparator<Amenity> comparator) {
        Objects.requireNonNull(comparator, "Comparator cannot be null");
        return amenities.stream()
                .sorted(comparator)
                .toList();
    }

    @Override
    public Optional<Amenity> findAmenityByName(String name) {
        Objects.requireNonNull(name, "Amenity name cannot be null");
        return amenities.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void updateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");

        int index = -1;
        for (int i = 0; i < amenities.size(); i++) {
            if (amenities.get(i).getName().equalsIgnoreCase(amenity.getName())) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            throw new IllegalArgumentException("Amenity '" + amenity.getName() + "' not found");
        }

        amenities.set(index, amenity);
    }

    @Override
    public void deleteAmenity(String amenityName) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        amenities.removeIf(a -> a.getName().equalsIgnoreCase(amenityName));
    }

    @Override
    public void clearAll() {
        amenities.clear();
    }
}