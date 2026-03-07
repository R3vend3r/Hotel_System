package hotel_system.service;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
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

    private final RoomService roomService;

    private final AmenityService amenityService;

    private final ClientService clientService;

    private final OrderService orderService;

    @Qualifier("roomCsvService")
    private final ICsvService<Room> roomCsvService;

    @Qualifier("amenityCsvService")
    private final ICsvService<Amenity> amenityCsvService;

    @Qualifier("clientCsvService")
    private final ICsvService<Client> clientCsvService;

    @Qualifier("roomBookingCsvService")
    private final ICsvService<RoomBooking> roomBookingCsvService;

    @Qualifier("amenityOrderCsvService")
    private final ICsvService<AmenityOrder> amenityOrderCsvService;

    @Autowired
    public CsvTestService(RoomService roomService, AmenityService amenityService, ClientService clientService, OrderService orderService,
                          ICsvService<Room> roomCsvService, ICsvService<Amenity> amenityCsvService, ICsvService<Client> clientCsvService,
                          ICsvService<RoomBooking> roomBookingCsvService, ICsvService<AmenityOrder> amenityOrderCsvService) {
        this.roomService = roomService;
        this.amenityService = amenityService;
        this.clientService = clientService;
        this.orderService = orderService;
        this.roomCsvService = roomCsvService;
        this.amenityCsvService = amenityCsvService;
        this.clientCsvService = clientCsvService;
        this.roomBookingCsvService = roomBookingCsvService;
        this.amenityOrderCsvService = amenityOrderCsvService;
    }

    public void exportRoomsToCsv(String filePath) throws DataExportException {
        List<RoomResponse> rooms = roomService.getAllRooms();
        List<Room> roomEntities = rooms.stream()
                .map(response -> new Room(
                        response.number(),
                        response.type(),
                        response.price(),
                        response.capacity(),
                        response.roomCondition(),
                        response.stars()
                ))
                .toList();
        roomCsvService.exportCsv(roomEntities, filePath);
    }
    public List<Room> importRoomsFromCsv(String filePath) throws DataImportException {
        try {
            List<Room> importedRooms = roomCsvService.importCsv(filePath);
            for (Room room : importedRooms) {
                RoomRequest request = new RoomRequest(
                        room.getNumber(),
                        room.getType(),
                        room.getPriceForDay(),
                        room.getCapacity()
                );
                roomService.addRoom(request);
            }
            return importedRooms;
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Ошибка при импорте комнат из CSV" , e);
        }
    }

    public void exportClientsToCsv(String filePath) throws DataExportException {
        List<ClientResponse> clients = clientService.getAllClients();
        List<Client> clientEntities = clients.stream()
                .map(response -> new Client(response.name(), response.surname()))
                .toList();
        clientCsvService.exportCsv(clientEntities, filePath);
    }

    public List<Client> importClientsFromCsv(String filePath) throws DataImportException {
        try {
            List<Client> importedClients = clientCsvService.importCsv(filePath);
            for (Client client : importedClients) {
                ClientRequest request = new ClientRequest(
                        client.getName(),
                        client.getSurname()
                );
                clientService.registerClient(request);
            }
            return importedClients;
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Ошибка при импорте клиентов из CSV", e);
        }
    }

    public void exportAmenitiesToCsv(String filePath) throws DataExportException {
        List<AmenityResponse> amenities = amenityService.getAllAmenities();
        List<Amenity> amenityEntities = amenities.stream()
                .map(response -> new Amenity(response.name(), response.price()))
                .toList();
        amenityCsvService.exportCsv(amenityEntities, filePath);
    }

    public List<Amenity> importAmenitiesFromCsv(String filePath) throws DataImportException {
        try {
            List<Amenity> importedAmenities = amenityCsvService.importCsv(filePath);
            for (Amenity amenity : importedAmenities) {
                AmenityRequest request = new AmenityRequest(
                        amenity.getName(),
                        amenity.getPrice()
                );
                amenityService.findAmenityByName(amenity.getName())
                        .ifPresentOrElse(
                                existing -> amenityService.updateAmenityPrice(amenity.getName(), amenity.getPrice()),
                                () -> amenityService.addAmenity(request)
                        );
            }
            return importedAmenities;
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Ошибка при импорте услуг из CSV", e);
        }
    }

    public void exportRoomBookingsToCsv(String filePath) throws DataExportException {
        List<RoomBookingResponse> activeBookings = orderService.getActiveBookingsSorted(SortType.NONE);
        List<RoomBookingResponse> completedBookings = orderService.getCompletedBookings();

        List<RoomBooking> allBookings = new ArrayList<>();
        activeBookings.forEach(booking -> {
            RoomBooking entity = new RoomBooking();
            allBookings.add(entity);
        });
        completedBookings.forEach(booking -> {
            RoomBooking entity = new RoomBooking();
            allBookings.add(entity);
        });

        roomBookingCsvService.exportCsv(allBookings, filePath);
    }

    public List<RoomBooking> importRoomBookingsFromCsv(String filePath) throws DataImportException {
        try {
            List<RoomBooking> importedBookings = roomBookingCsvService.importCsv(filePath);
            for (RoomBooking booking : importedBookings) {
                if (clientService.findClientById(booking.getClientId()).isEmpty()) {
                    ClientRequest clientRequest = new ClientRequest(
                            booking.getClient().getName(),
                            booking.getClient().getSurname()
                    );
                    clientService.registerClient(clientRequest);
                }

                ClientResponse clientResponse = clientService.findClientById(booking.getClientId())
                        .orElseThrow(() -> new DataImportException("Client not found after registration"));

                RoomResponse roomResponse = roomService.findRoom(booking.getRoomNumber())
                        .orElseThrow(() -> new DataImportException("Room not found"));

                SettleClientRequest settleRequest = new SettleClientRequest(
                        clientResponse.id(),
                        roomResponse.number(),
                        booking.getCheckOutDate()
                );

                orderService.settleClient(settleRequest);
            }
            return importedBookings;
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Ошибка при импорте бронирований из CSV", e);
        }
    }

    public void exportAmenityOrdersToCsv(String filePath) throws DataExportException {
        List<AmenityOrderResponse> orders = orderService.getAmenityOrdersSorted(SortType.NONE);
        List<AmenityOrder> orderEntities = orders.stream()
                .map(response -> {
                    AmenityOrder entity = new AmenityOrder();
                    entity.setId(response.id());
                    entity.setClientId(response.clientId());
                    entity.setAmenityId(response.amenityId());
                    entity.setServiceDate(response.serviceDate());
                    entity.setTotalPrice(response.totalPrice());
                    return entity;
                })
                .toList();
        amenityOrderCsvService.exportCsv(orderEntities, filePath);
    }

    public List<AmenityOrder> importAmenityOrdersFromCsv(String filePath) throws DataImportException {
        try {
            List<AmenityOrder> importedOrders = amenityOrderCsvService.importCsv(filePath);
            for (AmenityOrder order : importedOrders) {
                if (clientService.findClientById(order.getClientId()).isEmpty()) {
                    ClientRequest clientRequest = new ClientRequest(
                            order.getClient().getName(),
                            order.getClient().getSurname()
                    );
                    clientService.registerClient(clientRequest);
                }

                String amenityName = order.getAmenity().getName();
                if (amenityService.findAmenityByName(amenityName).isEmpty()) {
                    AmenityRequest request = new AmenityRequest(
                            amenityName,
                            order.getAmenity().getPrice()
                    );
                    amenityService.addAmenity(request);
                }

                AmenityResponse amenityResponse = amenityService.findAmenityByName(amenityName)
                        .orElseThrow(() -> new DataImportException("Amenity not found after creation"));

                AddAmenityRequest addAmenityRequest = new AddAmenityRequest(
                        order.getClient().getRoomNumber(),
                        amenityResponse.amenityId(),
                        order.getServiceDate()
                );

                orderService.addAmenityToBooking(addAmenityRequest);
            }
            return importedOrders;
        } catch (DataImportException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("Ошибка при импорте заказов услуг из CSV", e);
        }
    }

}
