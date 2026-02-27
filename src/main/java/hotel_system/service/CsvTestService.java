package hotel_system.service;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.enums.SortType;
import hotel_system.model.entity.*;
import hotel_system.service.csv.ICsvService;
import hotel_system.service.entityService.AmenityService;
import hotel_system.service.entityService.ClientService;
import hotel_system.service.entityService.OrderService;
import hotel_system.service.entityService.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CsvTestService {

    @Autowired
    private RoomService roomService;
    @Autowired
    private AmenityService amenityService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private OrderService orderService;

    @Autowired
    @Qualifier("roomCsvService")
    private ICsvService<Room> roomCsvService;

    @Autowired
    @Qualifier("amenityCsvService")
    private ICsvService<Amenity> amenityCsvService;

    @Autowired
    @Qualifier("clientCsvService")
    private ICsvService<Client> clientCsvService;

    @Autowired
    @Qualifier("roomBookingCsvService")
    private ICsvService<RoomBooking> roomBookingCsvService;

    @Autowired
    @Qualifier("amenityOrderCsvService")
    private ICsvService<AmenityOrder> amenityOrderCsvService;

    public void exportRoomsToCsv(String filePath) throws DataExportException {
        roomCsvService.exportCsv(roomService.getAllRooms(), filePath);
    }

    public List<Room> importRoomsFromCsv(String filePath) throws DataImportException {
        try {
            List<Room> importedRooms = roomCsvService.importCsv(filePath);
            for (Room room : importedRooms) {
                roomService.addRoom(room);
            }
            return importedRooms;
        } catch (Exception e) {
            throw new DataImportException("Ошибка при импорте комнат из CSV" , e);
        }
    }

    public void exportClientsToCsv(String filePath) throws DataExportException {
        clientCsvService.exportCsv(clientService.getAllClients(), filePath);
    }

    public List<Client> importClientsFromCsv(String filePath) throws DataImportException {
        try {
            List<Client> importedClients = clientCsvService.importCsv(filePath);
            for (Client client : importedClients) {
                clientService.registerClient(client);
            }
            return importedClients;
        } catch (Exception e) {
            throw new DataImportException("Ошибка при импорте клиентов из CSV", e);
        }
    }

    public void exportAmenitiesToCsv(String filePath) throws DataExportException {
        amenityCsvService.exportCsv(amenityService.getAllAmenities(), filePath);
    }

    public List<Amenity> importAmenitiesFromCsv(String filePath) throws DataImportException {
        try {
            List<Amenity> importedAmenities = amenityCsvService.importCsv(filePath);
            for (Amenity amenity : importedAmenities) {
                amenityService.findAmenityByName(amenity.getName()).ifPresentOrElse(
                        existing -> {
                            existing.setPrice(amenity.getPrice());
                            amenityService.updateAmenity(existing);
                        },
                        () -> amenityService.addAmenity(amenity)
                );
            }
            return importedAmenities;
        } catch (Exception e) {
            throw new DataImportException("Ошибка при импорте услуг из CSV", e);
        }
    }

    public void exportRoomBookingsToCsv(String filePath) throws DataExportException {
        List<RoomBooking> allBookings = new ArrayList<>();
        allBookings.addAll(orderService.getActiveBookingsSorted(SortType.NONE));
        allBookings.addAll(orderService.getCompletedBookings());
        roomBookingCsvService.exportCsv(allBookings, filePath);
    }

    public List<RoomBooking> importRoomBookingsFromCsv(String filePath) throws DataImportException {
        try {
            List<RoomBooking> importedBookings = roomBookingCsvService.importCsv(filePath);
            for (RoomBooking booking : importedBookings) {
                if (clientService.findClientById(booking.getClientId()).isEmpty()) {
                    clientService.registerClient(booking.getClient());
                }
                if (roomService.findRoom(booking.getRoom().getNumber()).isEmpty()) {
                    roomService.addRoom(booking.getRoom());
                }
                orderService.createRoomBooking(booking.getClient(), booking.getRoom(), booking.getCheckOutDate());
            }
            return importedBookings;
        } catch (Exception e) {
            throw new DataImportException("Ошибка при импорте бронирований из CSV", e);
        }
    }

    public void exportAmenityOrdersToCsv(String filePath) throws DataExportException {
        List<AmenityOrder> orders = new ArrayList<>(orderService.getAmenityOrdersSorted(SortType.NONE));
        amenityOrderCsvService.exportCsv(orders, filePath);
    }

    public List<AmenityOrder> importAmenityOrdersFromCsv(String filePath) throws DataImportException {
        try {
            List<AmenityOrder> importedOrders = amenityOrderCsvService.importCsv(filePath);
            for (AmenityOrder order : importedOrders) {
                if (clientService.findClientById(order.getClientId()).isEmpty()) {
                    clientService.registerClient(order.getClient());
                }
                if (amenityService.findAmenityByName(order.getAmenity().getName()).isEmpty()) {
                    amenityService.addAmenity(order.getAmenity());
                }
                orderService.addAmenityToBooking(order.getClient().getRoomNumber(), order.getAmenity(), order.getServiceDate());
            }
            return importedOrders;
        } catch (Exception e) {
            throw new DataImportException("Ошибка при импорте заказов услуг из CSV", e);
        }
    }

}
