package hotel_system.controller;

import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.AmenityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/amenities")
public class AmenityController {

    private final AmenityService amenityService;

    @Autowired
    public AmenityController(AmenityService amenityService) {
        this.amenityService = amenityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addAmenity(@RequestBody AmenityRequest request) {
        amenityService.addAmenity(request);
    }

    @GetMapping("/name/{name}")
    public Optional<AmenityResponse> findAmenityByName(@PathVariable String name) {
        return amenityService.findAmenityByName(name);
    }

    @GetMapping("/id/{id}")
    public Optional<AmenityResponse> findAmenityById(@PathVariable String id) {
        return amenityService.findAmenityById(id);
    }
    @GetMapping
    public List<AmenityResponse> getAmenities(
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return amenityService.getSortedAmenities(sortType);
    }

    @PatchMapping("/{amenityName}/price")
    public void updateAmenityPrice(
            @PathVariable String amenityName,
            @RequestParam double newPrice) {
        amenityService.updateAmenityPrice(amenityName, newPrice);
    }
}