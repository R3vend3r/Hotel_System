package hotel_system.model;

import hotel_system.Exception.DataExportException;
import hotel_system.Exception.DataImportException;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.model.entity.*;

import hotel_system.Utils.HotelConfig;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.SortType;
import hotel_system.service.csv.ICsvService;
import hotel_system.service.entityService.AmenityService;
import hotel_system.service.entityService.ClientService;
import hotel_system.service.entityService.OrderService;
import hotel_system.service.entityService.RoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ManagerHotel {
    private static final Logger logger = LoggerFactory.getLogger(ManagerHotel.class);

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

    @Autowired
    private HotelConfig hotelConfig;

    public void settleClient(Client client, Room room, Date checkOutDate) {
        try {
            validateSettlement(room);
            processClientRegistration(client);
            assignRoomToClient(client, room);
            createRoomBooking(client, room, checkOutDate);

            logger.info("Клиент {} заселен в комнату {}", client.getId(), room.getNumber());
        } catch (Exception e) {
            logger.error("Ошибка при заселении клиента", e);
            throw new ManagerHotelException("Ошибка при заселении клиента: " + e.getMessage(), e);
        }
    }

    public void evictClient(Integer roomNumber) {
        try {
            logger.info("Выселение из комнаты {}", roomNumber);

            Client client = findClientInRoom(roomNumber);
            processActiveBooking(roomNumber);
            vacateClientAndRoom(client, roomNumber);

            logger.info("Клиент выселен из комнаты {}", roomNumber);
        } catch (Exception e) {
            logger.error("Ошибка при выселении клиента", e);
            throw new ManagerHotelException("Ошибка при выселении клиента: " + e.getMessage(), e);
        }
    }

    public Optional<Client> findClientByRoom(Integer roomNumber) {
        return clientService.findClientByRoomNumber(roomNumber);
    }

    public Optional<Room> findRoom(int roomNumber) {
        return roomService.findRoom(roomNumber);
    }

    public Optional<Client> findClientById(String clientId) {
        return clientService.findClientById(clientId);
    }

    public Optional<Amenity> findAmenityByName(String name) {
        return amenityService.findAmenityByName(name);
    }

    public void registerClient(Client client) {
        try {
            clientService.registerClient(client);
        } catch (Exception e) {
            logger.error("Ошибка при регистрации клиента", e);
            throw new ManagerHotelException("Ошибка при регистрации клиента: " + e.getMessage(), e);
        }
    }

    public void addRoom(Room room) {
        try {
            roomService.addRoom(room);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении комнаты", e);
            throw new ManagerHotelException("Ошибка при добавлении комнаты: " + e.getMessage(), e);
        }
    }

    public void addAmenity(Amenity amenity) {
        try {
            amenityService.addAmenity(amenity);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении услуги", e);
            throw new ManagerHotelException("Ошибка при добавлении услуги: " + e.getMessage(), e);
        }
    }

    public void updateRoomStatus(int number, RoomCondition status) {
        if (hotelConfig.isRoomStatusChangeEnabled()) {
            try {
                roomService.updateRoomStatus(number, status);
            } catch (Exception e) {
                logger.error("Ошибка при обновлении статуса комнаты", e);
                throw new ManagerHotelException("Ошибка при обновлении статуса комнаты: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalStateException("Изменение статуса комнаты запрещено конфигурацией");
        }
    }

    public void updateRoomPrice(int number, double newPrice) {
        try {
            roomService.updateRoomPrice(number, newPrice);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении цены комнаты", e);
            throw new ManagerHotelException("Ошибка при обновлении цены комнаты: " + e.getMessage(), e);
        }
    }

    public void updateAmenityPrice(String amenityName, double newPrice) {
        try {
            amenityService.updateAmenityPrice(amenityName, newPrice);
        } catch (Exception e) {
            logger.error("Ошибка при обновлении цены услуги", e);
            throw new ManagerHotelException("Ошибка при обновлении цены услуги: " + e.getMessage(), e);
        }
    }

    public List<Room> getRooms(SortType sortType, boolean onlyAvailable) {
        return onlyAvailable
                ? roomService.getSortedAvailableRooms(sortType)
                : roomService.getSortedRooms(sortType);
    }

    public List<Client> getRoomHistory(int roomNumber) {
        try {
            List<RoomBooking> bookings = orderService.getAllBookingsForRoom(roomNumber);

            List<Client> clients = new ArrayList<>();
            for (RoomBooking booking : bookings) {
                Optional<Client> client = clientService.findClientById(booking.getClientId());
                client.ifPresent(clients::add);
            }

            return clients;
        } catch (Exception e) {
            logger.error("Ошибка при получении истории комнаты", e);
            throw new ManagerHotelException("Ошибка при получении истории комнаты: " + e.getMessage(), e);
        }
    }

    public List<Amenity> getAmenities(SortType sortType) {
        return switch (sortType) {
            case PRICE -> amenityService.getAmenitiesSortedByPrice();
            case ALPHABET -> amenityService.getAmenitiesSortedByName();
            case NONE -> amenityService.getAllAmenities();
            default -> throw new IllegalArgumentException("Unsupported sort type for amenities");
        };
    }

    public void addAmenityToClient(int roomNumber, Amenity amenity, Date serviceDate) {
        try {
            orderService.addAmenityToBooking(roomNumber, amenity, serviceDate);
        } catch (Exception e) {
            logger.error("Ошибка при добавлении услуги клиенту", e);
            throw new ManagerHotelException("Ошибка при добавлении услуги клиенту: " + e.getMessage(), e);
        }
    }

    public List<Room> getAvailableRoomsByDate(Date date) {
        return roomService.getAvailableRoomsByDate(date);
    }

    public double calculateRoomPayment(int roomNumber) {
        return orderService.calculateRoomPayment(roomNumber);
    }

    public double calculateTotalRevenue() {
        return orderService.calculateTotalRevenue();
    }

    public int getAvailableRoomsCount() {
        return roomService.countAvailableRooms();
    }

    public boolean isRoomAvailable(int number) {
        return roomService.isRoomAvailable(number);
    }

    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    public int getClientCount() {
        return clientService.getClientCount();
    }

    public List<RoomBooking> getAllActiveBookings(SortType sortType) {
        return orderService.getActiveBookingsSorted(sortType);
    }

    public List<RoomBooking> getAllCompletedBookings() {
        return orderService.getCompletedBookings();
    }

    public List<AmenityOrder> getClientAmenitiesSorted(Client client, SortType sortType) {
        return orderService.getAmenityOrdersSorted(sortType).stream()
                .filter(order -> order.getClientId().equals(client.getId()))
                .collect(Collectors.toList());
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        return orderService.getLastThreeBookingsForRoom(roomNumber);
    }

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


    private void validateSettlement(Room room) {
        if (!roomService.isRoomAvailable(room.getNumber())) {
            throw new IllegalStateException("Room " + room.getNumber() + " is not available");
        }
    }

    private void processClientRegistration(Client client) {
        if (client.getId() == null || clientService.findClientById(client.getId()).isEmpty()) {
            clientService.registerClient(client);
        }
    }

    private void assignRoomToClient(Client client, Room room) {
        roomService.occupyRoom(room.getNumber());
        clientService.assignClientToRoom(client.getId(), room.getNumber());
    }

    private void createRoomBooking(Client client, Room room, Date checkOutDate) {
        orderService.createRoomBooking(client, room, checkOutDate);
    }

    private Client findClientInRoom(int roomNumber) {
        Optional<Client> clientOpt = clientService.findClientByRoomNumber(roomNumber);
        if (clientOpt.isEmpty()) {
            throw new RuntimeException("В комнате " + roomNumber + " нет клиента");
        }

        Client client = clientOpt.get();
        logger.info("Найден клиент для выселения: {} {}",
                client.getName(), client.getSurname());
        return client;
    }

    private void processActiveBooking(int roomNumber) {
        Optional<RoomBooking> bookingOpt = orderService.getActiveBookingByRoom(roomNumber);

        if (bookingOpt.isPresent()) {
            logger.info("Завершение активного бронирования");
            orderService.evictClient(roomNumber);
        } else {
            logger.info("Активного бронирования для комнаты {} не найдено", roomNumber);
        }
    }

    private void vacateClientAndRoom(Client client, int roomNumber) {
        clientService.vacateClientFromRoom(client.getId());
        roomService.vacateRoom(roomNumber);
    }
}