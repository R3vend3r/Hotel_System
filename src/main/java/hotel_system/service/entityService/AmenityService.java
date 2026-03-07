package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import hotel_system.model.entity.Amenity;
import hotel_system.dao.AmenityDAO;
import hotel_system.model.mapper.AmenityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AmenityService {
    private static final Logger logger = LoggerFactory.getLogger(AmenityService.class);

    private final AmenityDAO amenityDAO;
    private final AmenityMapper amenityMapper;

    @Autowired
    public AmenityService(AmenityDAO amenityDAO, AmenityMapper amenityMapper) {
        this.amenityDAO =  amenityDAO;
        this.amenityMapper = amenityMapper;
    }

    @Transactional
    public void addAmenity(AmenityRequest request) {
        Objects.requireNonNull(request, "Amenity request cannot be null");
        try {
            Amenity amenityEntity = amenityMapper.toEntity(request);

            amenityDAO.create(amenityEntity);

            logger.info("Amenity '{}' added successfully with id: {}", amenityEntity.getName(), amenityEntity.getId());
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
    public List<AmenityResponse> getAllAmenities() {
        try {
            List<Amenity> amenities = amenityDAO.findAll();
            return amenities.stream()
                    .map(amenityMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            logger.error("Failed to get all amenities", e);
            throw new ServiceException("Failed to get all amenities", e);
        }
    }

    @Transactional(readOnly = true)
    public  List<AmenityResponse> getSortedAmenities(SortType sortType){
        try{
            List<Amenity> amenities = switch (sortType) {
                case PRICE -> amenityDAO.findAllSortedByPrice();
                case ALPHABET -> amenityDAO.findAllSortedByName();
                case NONE -> amenityDAO.findAll();
                default -> throw new IllegalArgumentException("Unsupported sort type for amenities");
            };

            return amenities.stream()
                    .map(amenityMapper::toResponse)
                    .collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Failed to get sorted amenities",e);
        }
    }


    @Transactional
    public void updateAmenity(AmenityRequest request, String amenityId) {
        Objects.requireNonNull(request, "Amenity request cannot be null");
        Objects.requireNonNull(amenityId, "Amenity ID cannot be null");

        try {
            Amenity existingAmenity = amenityDAO.findById(amenityId)
                    .orElseThrow(() -> new IllegalArgumentException("Amenity not found with id: " + amenityId));

            existingAmenity.setName(request.name());
            existingAmenity.setPrice(request.price());

            amenityDAO.update(existingAmenity);
            logger.info("Amenity with id '{}' updated successfully", amenityId);

        } catch (DaoException e) {
            logger.error("Failed to update amenity", e);
            throw new ServiceException("Failed to update amenity", e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<AmenityResponse> findAmenityByName(String name) {
        Objects.requireNonNull(name, "Amenity name cannot be null");
        try {
            return amenityDAO.findByName(name)
                    .map(amenityMapper::toResponse);
        } catch (DaoException e) {
            logger.error("Failed to find amenity by name", e);
            throw new ServiceException("Failed to find amenity by name", e);
        }
    }
    @Transactional(readOnly = true)
    public Optional<AmenityResponse> findAmenityById(String id) {
        Objects.requireNonNull(id, "Amenity name cannot be null");
        try {
            return amenityDAO.findById(id)
                    .map(amenityMapper::toResponse);
        } catch (DaoException e) {
            logger.error("Failed to find amenity by name", e);
            throw new ServiceException("Failed to find amenity by name", e);
        }
    }
}