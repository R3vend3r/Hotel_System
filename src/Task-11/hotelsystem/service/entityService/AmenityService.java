package hotelsystem.service.entityService;

import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.dependencies.annotation.PostConstruct;
import hotelsystem.model.Amenity;
import hotelsystem.dao.AmenityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class AmenityService {
    private static final Logger logger = LoggerFactory.getLogger(AmenityService.class);

    @Inject
    private AmenityDAO amenityDAO;

    @PostConstruct
    public void postConstruct() {
        logger.info("AmenityService initialized");
    }

    public void addAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.create(amenity);
        } catch (Exception e) {
            logger.error("Failed to add amenity", e);
            throw new RuntimeException("Failed to add amenity", e);
        }
    }

    public boolean doesAmenityExist(String amenityName) {
        try {
            Optional<Amenity> amenity = amenityDAO.findByName(amenityName);
            return amenity.isPresent();
        } catch (Exception e) {
            logger.error("Failed to check amenity existence", e);
            throw new RuntimeException("Failed to check amenity existence", e);
        }
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        try {
            Amenity amenity = amenityDAO.findByName(amenityName)
                    .orElseThrow(() -> new IllegalArgumentException("Amenity not found"));
            amenity.setPrice(newPrice);
            amenityDAO.update(amenity);
        } catch (Exception e) {
            logger.error("Failed to update amenity price", e);
            throw new RuntimeException("Failed to update amenity price", e);
        }
    }

    public List<Amenity> getAllAmenities() {
        try {
            return amenityDAO.findAll();
        } catch (Exception e) {
            logger.error("Failed to get all amenities", e);
            throw new RuntimeException("Failed to get all amenities", e);
        }
    }

    public List<Amenity> getAmenitiesSortedByPrice() {
        try {
            return amenityDAO.findAllSortedByPrice();
        } catch (Exception e) {
            logger.error("Failed to get amenities sorted by price", e);
            throw new RuntimeException("Failed to get amenities sorted by price", e);
        }
    }

    public List<Amenity> getAmenitiesSortedByName() {
        List<Amenity> amenities = getAllAmenities();
        amenities.sort(Comparator.comparing(Amenity::getName));
        return amenities;
    }

    public void updateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.update(amenity);
        } catch (Exception e) {
            logger.error("Failed to update amenity", e);
            throw new RuntimeException("Failed to update amenity", e);
        }
    }

    public Optional<Amenity> findAmenityByName(String name) {
        Objects.requireNonNull(name, "Amenity name cannot be null");
        try {
            return amenityDAO.findByName(name);
        } catch (Exception e) {
            logger.error("Failed to find amenity by name", e);
            throw new RuntimeException("Failed to find amenity by name", e);
        }
    }

    public void deleteAmenity(String amenityName) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        try {
            Optional<Amenity> amenityOpt = amenityDAO.findByName(amenityName);
            if (amenityOpt.isPresent()) {
                Amenity amenity = amenityOpt.get();
                amenityDAO.delete(amenity.getId());
            }
        } catch (Exception e) {
            logger.error("Failed to delete amenity", e);
            throw new RuntimeException("Failed to delete amenity", e);
        }
    }
}