package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.ServiceException;
import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.model.entity.*;
import hotel_system.comparator.AmenityComparator.DateAmenComparator;
import hotel_system.comparator.AmenityComparator.PriceAmenComparator;
import hotel_system.comparator.OrderCorparator.AlphabetComparator;
import hotel_system.comparator.OrderCorparator.DateComparator;
import hotel_system.comparator.OrderCorparator.NoneComparator;
import hotel_system.dao.AmenityOrderDAO;
import hotel_system.dao.RoomBookingDAO;
import hotel_system.enums.SortType;
import hotel_system.model.mapper.AmenityOrderMapper;
import hotel_system.model.mapper.RoomBookingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final long MINIMUM_DAYS = 1;
    private static final long EVICTION_DELAY_MS = 1000;

    private final RoomService roomService;
    private final ClientService clientService;
    private final BookingService bookingService;
    private final RoomBookingDAO roomBookingDAO;
    private final AmenityService amenityService;
    private final AmenityOrderDAO amenityOrderDAO;
    private final RoomBookingMapper roomBookingMapper;
    private final AmenityOrderMapper amenityOrderMapper;

    @Autowired
    public OrderService(RoomBookingDAO roomBookingDAO,
                        AmenityOrderDAO amenityOrderDAO,
                        ClientService clientService,
                        RoomService roomService,
                        AmenityService amenityService,
                        RoomBookingMapper roomBookingMapper,
                        AmenityOrderMapper amenityOrderMapper,
                        BookingService bookingService) {
        this.roomBookingDAO = roomBookingDAO;
        this.amenityOrderDAO = amenityOrderDAO;
        this.clientService = clientService;
        this.roomService = roomService;
        this.amenityService = amenityService;
        this.amenityOrderMapper = amenityOrderMapper;
        this.roomBookingMapper = roomBookingMapper;
        this.bookingService = bookingService;
    }

    @Transactional
    public void settleClient(SettleClientRequest request) {
        try {
            ClientResponse client = clientService.findClientById(request.clientId())
                    .orElseThrow(() -> new ServiceException("Client not found: " + request.clientId()));

            RoomResponse room = roomService.findRoom(request.roomNumber())
                    .orElseThrow(() -> new ServiceException("Room not found: " + request.roomNumber()));

            if (!roomService.isRoomAvailable(room.number())) {
                throw new IllegalStateException("Room " + room.number() + " is not available");
            }

            Client clientEntity = clientService.findClientEntityById(client.id())
                    .orElseThrow(() -> new ServiceException("Client entity not found"));

            Room roomEntity = roomService.findRoomEntity(room.number())
                    .orElseThrow(() -> new ServiceException("Room entity not found"));

            roomService.occupyRoom(room.number());

            RoomBooking booking = createBookingObject(clientEntity, roomEntity, request.checkOutDate());
            roomBookingDAO.create(booking);

            logger.info("Клиент {} заселен в комнату {}", client.id(), room.number());

        } catch (DaoException e) {
            logger.error("Failed to settle client", e);
            throw new ServiceException("Failed to settle client", e);
        }
    }

    @Transactional
    public void addAmenityToBooking(AddAmenityRequest request) {
        Objects.requireNonNull(request, "Request cannot be null");

        try {
            AmenityResponse amenityResponse = amenityService.findAmenityById(request.amenityId())
                    .orElseThrow(() -> new ServiceException("Amenity not found: " + request.amenityId()));

            ClientResponse clientResponse = clientService.findClientById(request.clientId())
                    .orElseThrow(() -> new ServiceException("Client not found: " + request.clientId()));

            // Проверяем наличие активного бронирования через BookingService
            Optional<RoomBooking> activeBooking = bookingService.findActiveBookingEntityByClientId(request.clientId());
            if (activeBooking.isEmpty()) {
                throw new ServiceException("No active booking found for client: " + request.clientId());
            }

            Amenity amenityEntity = convertToEntity(amenityResponse);
            Client clientEntity = convertToEntity(clientResponse);

            addAmenityAndUpdateBooking(clientEntity, amenityEntity, request.serviceDate());

            logger.info("Услуга '{}' добавлена для клиента {}", amenityResponse.name(), request.clientId());

        } catch (DaoException e) {
            logger.error("Failed to add amenity to booking", e);
            throw new ServiceException("Failed to add amenity to booking", e);
        }
    }

    @Transactional
    public void evictClient(Integer roomNumber) {
        try {
            // Используем BookingService для поиска клиента по комнате
            Optional<Client> clientOpt = bookingService.findClientByRoom(roomNumber);

            if (clientOpt.isEmpty()) {
                logger.warn("Не удалось выселить клиента: комната {} не найдена или пуста", roomNumber);
                throw new ServiceException("Room " + roomNumber + " is empty or not found");
            }

            Client client = clientOpt.get();

            // Завершаем бронирование
            findAndUpdateBooking(roomNumber);

            roomService.vacateRoom(roomNumber);

            logger.info("Клиент {} успешно выселен из комнаты {}", client.getId(), roomNumber);

        } catch (ServiceException e) {
            logger.error("Business error while evicting client from room {}: {}", roomNumber, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to evict client from room {}", roomNumber, e);
            throw new ServiceException("Failed to evict client from room " + roomNumber, e);
        }
    }

    @Transactional(readOnly = true)
    public double calculateRoomPayment(int roomNumber) {
        try {
            double stayCost = roomBookingDAO.calculateStayCost(roomNumber);
            double amenityCost = amenityOrderDAO.calculateTotalForRoom(roomNumber);

            return stayCost + amenityCost;
        } catch (DaoException e) {
            logger.error("Failed to calculate room payment", e);
            throw new ServiceException("Failed to calculate room payment", e);
        }
    }

    @Transactional(readOnly = true)
    public double calculateTotalRevenue() {
        try {
            double bookingIncome = roomBookingDAO.calculateTotalIncome();
            double amenityIncome = amenityOrderDAO.calculateTotalIncome();
            return bookingIncome + amenityIncome;
        } catch (DaoException e) {
            logger.error("Failed to calculate total revenue", e);
            throw new ServiceException("Failed to calculate total revenue", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RoomBookingResponse> getActiveBookingsSorted(SortType sortType) {
        try {
            List<RoomBooking> bookings = roomBookingDAO.findActiveBookings();
            List<RoomBooking> sortedBookings = sortBookings(bookings, sortType);
            return sortedBookings.stream()
                    .map(roomBookingMapper::toResponse)
                    .toList();
        } catch (DaoException e) {
            logger.error("Failed to get active bookings", e);
            throw new ServiceException("Failed to get active bookings", e);
        }
    }

    @Transactional(readOnly = true)
    public List<RoomBookingResponse> getCompletedBookings() {
        try {
            return roomBookingDAO.findCompletedBookings()
                    .stream()
                    .map(roomBookingMapper::toResponse)
                    .toList();
        } catch (DaoException e) {
            logger.error("Failed to get completed bookings", e);
            throw new ServiceException("Failed to get completed bookings", e);
        }
    }

    @Transactional(readOnly = true)
    public List<AmenityOrderResponse> getAmenityOrdersSorted(SortType sortType) {
        try {
            List<AmenityOrder> orders = amenityOrderDAO.findAll();
            List<AmenityOrder> sortedOrders = sortAmenityOrders(orders, sortType);
            return sortedOrders.stream()
                    .map(amenityOrderMapper::toResponse)
                    .toList();
        } catch (DaoException e) {
            logger.error("Failed to get amenity orders", e);
            throw new ServiceException("Failed to get amenity orders", e);
        }
    }

    @Transactional(readOnly = true)
    public List<AmenityOrderResponse> getClientAmenitiesSorted(String clientId, SortType sortType) {
        try {
            List<AmenityOrder> orders = amenityOrderDAO.findAll();
            List<AmenityOrder> clientOrders = orders.stream()
                    .filter(order -> clientId.equals(order.getClientId()))
                    .toList();
            List<AmenityOrder> sortedOrders = sortAmenityOrders(clientOrders, sortType);
            return sortedOrders.stream()
                    .map(amenityOrderMapper::toResponse)
                    .toList();
        } catch (DaoException e) {
            logger.error("Failed to get amenity orders for client: {}", clientId, e);
            throw new ServiceException("Failed to get amenity orders for client: " + clientId, e);
        }
    }

    @Transactional(readOnly = true)
    public List<RoomBookingResponse> getLastThreeBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findByRoom(roomNumber, 3)
                    .stream()
                    .map(roomBookingMapper::toResponse)
                    .toList();
        } catch (DaoException e) {
            logger.error("Failed to get last three bookings for room", e);
            throw new ServiceException("Failed to get last three bookings for room", e);
        }
    }

    @Transactional(readOnly = true)
    public List<ClientResponse> getRoomHistory(int roomNumber) {
        try {
            List<RoomBooking> bookings = roomBookingDAO.findAllByRoom(roomNumber);

            return bookings.stream()
                    .map(RoomBooking::getClient)
                    .map(client -> new ClientResponse(client.getId(), client.getName(), client.getSurname()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Ошибка при получении истории комнаты", e);
            throw new ManagerHotelException("Ошибка при получении истории комнаты: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Integer> getRoomNumberByClientId(String clientId) {
        return bookingService.findRoomByClientId(clientId);
    }

    private List<RoomBooking> sortBookings(List<RoomBooking> bookings, SortType sortType) {
        if (bookings == null || bookings.isEmpty()) {
            return Collections.emptyList();
        }
        List<RoomBooking> sortedBookings = new ArrayList<>(bookings);
        switch (sortType) {
            case DATE_END:
                sortedBookings.sort(new DateComparator());
                break;
            case ALPHABET:
                sortedBookings.sort(new AlphabetComparator());
                break;
            case NONE:
                sortedBookings.sort(new NoneComparator());
                break;
            default:
                break;
        }
        return sortedBookings;
    }

    private List<AmenityOrder> sortAmenityOrders(List<AmenityOrder> orders, SortType sortType) {
        if (orders == null) {
            return Collections.emptyList();
        }
        List<AmenityOrder> sortedOrders = new ArrayList<>(orders);
        switch (sortType) {
            case DATE_END:
                sortedOrders.sort(new DateAmenComparator());
                break;
            case PRICE:
                sortedOrders.sort(new PriceAmenComparator());
                break;
            case NONE:
                sortedOrders.sort(new NoneComparator());
                break;
            default:
                break;
        }
        return sortedOrders;
    }

    private double calculateStayCost(double pricePerDay, Date start, Date end) {
        long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
        return pricePerDay * Math.max(MINIMUM_DAYS, days);
    }

    private RoomBooking createBookingObject(Client client, Room room, Date checkOutDate) {
        Date checkInDate = new Date();
        double totalPrice = calculateStayCost(room.getPriceForDay(), checkInDate, checkOutDate);

        return new RoomBooking(
                client,
                room,
                totalPrice,
                checkInDate,
                checkOutDate
        );
    }

    private void findAndUpdateBooking(int roomNumber) throws DaoException {
        Optional<RoomBooking> bookingOpt = roomBookingDAO.findActiveByRoom(roomNumber);
        if (bookingOpt.isEmpty()) {
            throw new ServiceException("No active booking for room " + roomNumber);
        }

        RoomBooking booking = bookingOpt.get();
        Date checkOutDate = new Date(System.currentTimeMillis() + EVICTION_DELAY_MS);
        booking.setCheckOutDate(checkOutDate);
        roomBookingDAO.update(booking);
    }

    private void addAmenityAndUpdateBooking(Client client, Amenity amenity, Date serviceDate)
            throws DaoException {

        RoomBooking activeBooking = roomBookingDAO.findActiveByClientId(client.getId())
                .orElseThrow(() -> new ServiceException("No active booking found for client: " + client.getId()));

        AmenityOrder amenityOrder = new AmenityOrder(
                client.getId(),
                amenity.getPrice(),
                amenity.getId(),
                serviceDate
        );
        amenityOrderDAO.create(amenityOrder);

        double newTotal = activeBooking.getTotalPrice() + amenity.getPrice();
        activeBooking.setTotalPrice(newTotal);
        roomBookingDAO.update(activeBooking);
    }

    private Amenity convertToEntity(AmenityResponse response) {
        return new Amenity(
                response.amenityId(),
                response.name(),
                response.price()
        );
    }

    private Client convertToEntity(ClientResponse response) {
        return new Client(
                response.id(),
                response.name(),
                response.surname()
        );
    }
}