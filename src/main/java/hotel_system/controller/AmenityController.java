package hotel_system.controller;

import hotel_system.enums.SortType;
import hotel_system.model.entity.Amenity;
import hotel_system.service.entityService.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class AmenityController {

    private final AmenityService amenityService;

    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    public void addAmenity(Amenity amenity) {
        amenityService.addAmenity(amenity);
    }

    public Optional<Amenity> findAmenityByName(String name) {
        return amenityService.findAmenityByName(name);
    }

    public List<Amenity> getAmenities(SortType sortType) {
        return amenityService.getSortedAmenities(sortType);
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        amenityService.updateAmenityPrice(amenityName, newPrice);
    }
}