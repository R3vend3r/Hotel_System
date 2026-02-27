package hotel_system.controller;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.enums.SortType;
import hotel_system.model.entity.*;
import hotel_system.service.CsvTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CsvTestController {

    private final CsvTestService csvTestService;

    @Autowired
    public CsvTestController(CsvTestService csvTestService) {
        this.csvTestService = csvTestService;
    }

    public void exportAmenityOrdersToCsv(String filePath) throws DataExportException {
        csvTestService.exportAmenityOrdersToCsv(filePath);
    }

    public void exportAmenitiesToCsv(String filePath) throws DataExportException {
        csvTestService.exportAmenitiesToCsv(filePath);
    }

    public void exportClientsToCsv(String filePath) throws DataExportException {
        csvTestService.exportClientsToCsv(filePath);
    }
    public void exportRoomsToCsv(String filePath) throws DataExportException {
        csvTestService.exportRoomsToCsv(filePath);
    }

    public void exportRoomBookingsToCsv(String filePath) throws DataExportException {
        csvTestService.exportRoomBookingsToCsv(filePath);
    }

    public List<AmenityOrder> importAmenityOrdersFromCsv(String filePath) throws DataImportException {
           return csvTestService.importAmenityOrdersFromCsv(filePath);
    }

    public List<Amenity> importAmenitiesFromCsv(String filePath) throws DataImportException {
        return csvTestService.importAmenitiesFromCsv(filePath);
    }

    public List<Client> importClientsFromCsv(String filePath) throws DataImportException {
        return csvTestService.importClientsFromCsv(filePath);
    }

    public List<Room> importRoomsFromCsv(String filePath) throws DataImportException {
        return csvTestService.importRoomsFromCsv(filePath);
    }

    public List<RoomBooking> importRoomBookingsFromCsv(String filePath) throws DataImportException {
        return csvTestService.importRoomBookingsFromCsv(filePath);
    }

}
