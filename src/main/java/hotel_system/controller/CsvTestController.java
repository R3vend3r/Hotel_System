package hotel_system.controller;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.model.entity.*;
import hotel_system.service.CsvTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/csv")
public class CsvTestController {
    private final CsvTestService csvTestService;

    @Autowired
    public CsvTestController(CsvTestService csvTestService) {
        this.csvTestService = csvTestService;
    }

    @PostMapping("/export/amenity-orders")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void exportAmenityOrdersToCsv(@RequestParam String filePath) throws DataExportException {
        csvTestService.exportAmenityOrdersToCsv(filePath);
    }

    @PostMapping("/export/amenities")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void exportAmenitiesToCsv(@RequestParam String filePath) throws DataExportException {
        csvTestService.exportAmenitiesToCsv(filePath);
    }

    @PostMapping("/export/clients")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void exportClientsToCsv(@RequestParam String filePath) throws DataExportException {
        csvTestService.exportClientsToCsv(filePath);
    }

    @PostMapping("/export/rooms")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void exportRoomsToCsv(@RequestParam String filePath) throws DataExportException {
        csvTestService.exportRoomsToCsv(filePath);
    }

    @PostMapping("/export/room-bookings")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public void exportRoomBookingsToCsv(@RequestParam String filePath) throws DataExportException {
        csvTestService.exportRoomBookingsToCsv(filePath);
    }

    @PostMapping("/import/amenity-orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AmenityOrder>> importAmenityOrdersFromCsv(@RequestParam String filePath) throws DataImportException {
        List<AmenityOrder> result = csvTestService.importAmenityOrdersFromCsv(filePath);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/amenities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Amenity>> importAmenitiesFromCsv(@RequestParam String filePath) throws DataImportException {
        List<Amenity> result = csvTestService.importAmenitiesFromCsv(filePath);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/clients")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Client>> importClientsFromCsv(@RequestParam String filePath) throws DataImportException {
        List<Client> result = csvTestService.importClientsFromCsv(filePath);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Room>> importRoomsFromCsv(@RequestParam String filePath) throws DataImportException {
        List<Room> result = csvTestService.importRoomsFromCsv(filePath);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/import/room-bookings")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomBooking>> importRoomBookingsFromCsv(@RequestParam String filePath) throws DataImportException {
        List<RoomBooking> result = csvTestService.importRoomBookingsFromCsv(filePath);
        return ResponseEntity.ok(result);
    }
}