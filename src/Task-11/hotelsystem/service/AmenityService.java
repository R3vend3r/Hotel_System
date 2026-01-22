package hotelsystem.service;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;
import hotelsystem.interfaceClass.IAmenityRepository;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.model.Amenity;
import hotelsystem.repo.dao.AmenityDAO;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AmenityService implements IClearable {

    @Inject
    private IAmenityRepository amenityRepository;

    @Inject
    private AmenityDAO amenityDAO;

    @PostConstruct
    public void postConstruct() {
        System.out.println("AmenityService initialized with repository: "
                + amenityRepository.getClass().getSimpleName());
    }

    public void addAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.create(amenity);
            amenityRepository.addAmenity(amenity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add amenity", e);
        }
    }

    public boolean doesAmenityExist(String amenityName) {
        try {
            Optional<Amenity> amenity = amenityRepository.findAmenityByName(amenityName);
            if (amenity.isEmpty()) {
                amenity = amenityDAO.findAll().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(amenityName))
                        .findFirst();
                amenity.ifPresent(amenityRepository::addAmenity);
            }
            return amenity.isPresent();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check amenity existence", e);
        }
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        try {
            Amenity amenity = amenityRepository.findAmenityByName(amenityName)
                    .orElseThrow(() -> new IllegalArgumentException("Amenity not found"));
            amenity.setPrice(newPrice);
            amenityDAO.update(amenity);
            amenityRepository.updateAmenityPrice(amenityName, newPrice);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update amenity price", e);
        }
    }

    public List<Amenity> getAllAmenities() {
        try {
            List<Amenity> amenities = amenityDAO.findAll();
            amenities.forEach(amenityRepository::addAmenity);
            return amenityRepository.getAllAmenities();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all amenities", e);
        }
    }

    public List<Amenity> getAmenitiesSortedByPrice() {
        return amenityRepository.getSortedAmenities(Comparator.comparingDouble(Amenity::getPrice));
    }

    public List<Amenity> getAmenitiesSortedByName() {
        return amenityRepository.getSortedAmenities(Comparator.comparing(Amenity::getName));
    }

    public void updateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.update(amenity);
            amenityRepository.updateAmenity(amenity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update amenity", e);
        }
    }

    public Optional<Amenity> findAmenityByName(String name) {
        Objects.requireNonNull(name, "Amenity name cannot be null");
        try {
            Optional<Amenity> amenity = amenityRepository.findAmenityByName(name);
            if (amenity.isEmpty()) {
                amenity = amenityDAO.findAll().stream()
                        .filter(a -> a.getName().equalsIgnoreCase(name))
                        .findFirst();
                amenity.ifPresent(amenityRepository::addAmenity);
            }
            return amenity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to find amenity by name", e);
        }
    }
    public void deleteAmenity(String amenityName) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        try {
            amenityRepository.findAmenityByName(amenityName)
                    .ifPresent(amenity -> {
                        try {
                            amenityDAO.delete(amenity.getId());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
            amenityRepository.deleteAmenity(amenityName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete amenity", e);
        }
    }
    @Override
    public void clear() {
        try {
            amenityDAO.findAll().forEach(a -> {
                try {
                    amenityDAO.delete(a.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            amenityRepository.clearAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear amenities", e);
        }
    }
}