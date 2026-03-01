package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Amenity;
import hotel_system.dao.AmenityDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AmenityService {
    private static final Logger logger = LoggerFactory.getLogger(AmenityService.class);

    private final AmenityDAO amenityDAO;

    @Autowired
    public AmenityService(AmenityDAO amenityDAO) {
        this.amenityDAO =  amenityDAO;
    }

    @Transactional
    public void addAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.create(amenity);
        } catch (DaoException e) {
            logger.error("Failed to add amenity", e);
            throw new ServiceException("Failed to add amenity", e);
        }
    }

    @Transactional
    public void updateAmenityPrice(String amenityName, double newPrice) {
        Objects.requireNonNull(amenityName, "Amenity name cannot be null");
        try {
            Amenity amenity = amenityDAO.findByName(amenityName)
                    .orElseThrow(() -> new IllegalArgumentException("Amenity not found"));
            amenity.setPrice(newPrice);
            amenityDAO.update(amenity);
        } catch (DaoException e) {
            logger.error("Failed to update amenity price", e);
            throw new ServiceException("Failed to update amenity price", e);
        }
    }

    @Transactional(readOnly = true)
    public List<Amenity> getAllAmenities() {
        try {
            return amenityDAO.findAll();
        } catch (DaoException e) {
            logger.error("Failed to get all amenities", e);
            throw new ServiceException("Failed to get all amenities", e);
        }
    }

    @Transactional(readOnly = true)
    public  List<Amenity> getSortedAmenities(SortType sortType){
        return switch (sortType) {
            case PRICE -> amenityDAO.findAllSortedByPrice();
            case ALPHABET ->  amenityDAO.findAllSortedByName();
            case NONE -> amenityDAO.findAll();
            default -> throw new IllegalArgumentException("Unsupported sort type for amenities");
        };
    }

    @Transactional
    public void updateAmenity(Amenity amenity) {
        Objects.requireNonNull(amenity, "Amenity cannot be null");
        try {
            amenityDAO.update(amenity);
        } catch (DaoException e) {
            logger.error("Failed to update amenity", e);
            throw new ServiceException("Failed to update amenity", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Amenity> findAmenityByName(String name) {
        Objects.requireNonNull(name, "Amenity name cannot be null");
        try {
            return amenityDAO.findByName(name);
        } catch (DaoException e) {
            logger.error("Failed to find amenity by name", e);
            throw new ServiceException("Failed to find amenity by name", e);
        }
    }
}