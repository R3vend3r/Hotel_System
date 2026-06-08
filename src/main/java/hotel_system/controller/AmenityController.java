package hotel_system.controller;

import hotel_system.dto.AmenityRequest;
import hotel_system.dto.AmenityResponse;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.AmenityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ADMIN')")
    public void addAmenity(@Valid @RequestBody AmenityRequest request) {
        amenityService.addAmenity(request);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AmenityResponse> findAmenityByName(@PathVariable String name) {
        return amenityService.findAmenityByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<AmenityResponse> findAmenityById(@PathVariable String id) {
        return amenityService.findAmenityById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<AmenityResponse> getAmenities(
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return amenityService.getSortedAmenities(sortType);
    }

    @PatchMapping("/{amenityName}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateAmenityPrice(
            @PathVariable String amenityName,
            @RequestParam double newPrice) {
        amenityService.updateAmenityPrice(amenityName, newPrice);
    }
}