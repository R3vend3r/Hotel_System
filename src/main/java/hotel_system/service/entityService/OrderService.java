package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ManagerHotelException;
import hotel_system.Exception.ServiceException;
import hotel_system.model.entity.*;
import hotel_system.comparator.AmenityComparator.DateAmenComparator;
import hotel_system.comparator.AmenityComparator.PriceAmenComparator;
import hotel_system.comparator.OrderCorparator.AlphabetComparator;
import hotel_system.comparator.OrderCorparator.DateComparator;
import hotel_system.comparator.OrderCorparator.NoneComparator;
import hotel_system.dao.AmenityOrderDAO;
import hotel_system.dao.RoomBookingDAO;
import hotel_system.enums.SortType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private final RoomBookingDAO roomBookingDAO;

    @Autowired
    private final AmenityOrderDAO amenityOrderDAO;

    public OrderService(RoomBookingDAO roomBookingDAO, AmenityOrderDAO amenityOrderDAO) {
        this.roomBookingDAO = roomBookingDAO;
        this.amenityOrderDAO = amenityOrderDAO;
    }

    @Transactional
    public void createRoomBooking(Client client, Room room, Date checkOutDate) {
        try {
            if (!roomService.isRoomAvailable(room.getNumber())) {
                throw new IllegalStateException("Room " + room.getNumber() + " is not available");
            }
            if (client.getId() == null || clientService.findClientById(client.getId()).isEmpty()) {
                clientService.registerClient(client);
            }
            roomService.occupyRoom(room.getNumber());
            clientService.assignClientToRoom(client.getId(), room.getNumber());

            RoomBooking booking = createBookingObject(client, room, checkOutDate);
            roomBookingDAO.create(booking);
            logger.info("Бронирование создано для клиента {} в комнате {}",
                    client.getId(), room.getNumber());
        } catch (Exception e) {
            logger.error("Failed to create booking", e);
            throw new ServiceException("Failed to create booking", e);
        }
    }
    @Transactional
    public void addAmenityToBooking(Integer roomNumber, Amenity amenity, Date serviceDate) {
        try {
            RoomBooking booking = findActiveBooking(roomNumber);
            addAmenityAndUpdateBooking(booking, amenity, serviceDate);
            logger.info("Услуга '{}' добавлена к бронированию в комнате {}",
                    amenity.getName(), roomNumber);
        } catch (Exception e) {
            logger.error("Failed to add amenity to booking", e);
            throw new ServiceException("Failed to add amenity to booking", e);
        }
    }
    @Transactional
    public void evictClient(Integer roomNumber) {
        try {
            Optional<Client> clientOpt = clientService.findClientByRoomNumber(roomNumber);
            Optional<RoomBooking> bookingOpt = getActiveBookingByRoom(roomNumber);

            if (clientOpt.isPresent() && bookingOpt.isPresent()) {
                Client client = clientOpt.get();
                findAndUpdateBooking(roomNumber);
                clientService.vacateClientFromRoom(client.getId());
                roomService.vacateRoom(roomNumber);
                logger.info("Клиент {} выселен из комнаты {}", client.getId(), roomNumber);
            } else {
                logger.warn("Не удалось выселить клиента: комната {} не найдена или пуста", roomNumber);
            }
        } catch (Exception e) {
            logger.error("Failed to evict client from room {}", roomNumber, e);
            throw new ServiceException("Failed to evict client", e);
        }
    }

    public Optional<RoomBooking> getActiveBookingByRoom(int roomNumber) {
        try {
            return roomBookingDAO.findActiveByRoom(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to get active booking", e);
            throw new ServiceException("Failed to get active booking", e);
        }
    }

    public double calculateRoomPayment(int roomNumber) {
        try {
            double stayCost = roomBookingDAO.calculateStayCost(roomNumber, new Date());
            double amenityCost = amenityOrderDAO.calculateTotalForRoom(roomNumber);

            return stayCost + amenityCost;
        } catch (DaoException e) {
            logger.error("Failed to calculate room payment", e);
            throw new ServiceException("Failed to calculate room payment", e);
        }
    }

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

    public List<RoomBooking> getActiveBookingsSorted(SortType sortType) {
        try {
            List<RoomBooking> bookings = roomBookingDAO.findActiveBookings();
            return sortBookings(bookings, sortType);
        } catch (DaoException e) {
            logger.error("Failed to get active bookings", e);
            throw new ServiceException("Failed to get active bookings", e);
        }
    }

    public List<RoomBooking> getCompletedBookings() {
        try {
            return roomBookingDAO.findCompletedBookings();
        } catch (DaoException e) {
            logger.error("Failed to get completed bookings", e);
            throw new ServiceException("Failed to get completed bookings", e);
        }
    }

    public List<AmenityOrder> getAmenityOrdersSorted(SortType sortType) {
        try {
            List<AmenityOrder> orders = amenityOrderDAO.findAll();
            return sortAmenityOrders(orders, sortType);
        } catch (DaoException e) {
            logger.error("Failed to get amenity orders", e);
            throw new ServiceException("Failed to get amenity orders", e);
        }
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findByRoom(roomNumber, 3);
        } catch (DaoException e) {
            logger.error("Failed to get last three bookings for room", e);
            throw new ServiceException("Failed to get last three bookings for room", e);
        }
    }

    public List<RoomBooking> getAllBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findAllByRoom(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to get all bookings for room {}", roomNumber, e);
            throw new ServiceException("Failed to get all bookings for room " + roomNumber, e);
        }
    }

    public List<Client> getRoomHistory(int roomNumber){
        try {
            List<RoomBooking> bookings = getAllBookingsForRoom(roomNumber);

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
            default:
                break;
        }
        return sortedOrders;
    }

    private String generateId() {
        return "RB-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toLowerCase();
    }

    private double calculateStayCost(double pricePerDay, Date start, Date end) {
        long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
        return pricePerDay * Math.max(1, days);
    }

    private RoomBooking createBookingObject(Client client, Room room, Date checkOutDate) {
        Date checkInDate = new Date();
        double totalPrice = calculateStayCost(room.getPriceForDay(), checkInDate, checkOutDate);

        return new RoomBooking(
                generateId(),
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
        Date checkOutDate = new Date(System.currentTimeMillis() + 1000);
        booking.setCheckOutDate(checkOutDate);
        roomBookingDAO.update(booking);
    }

    private RoomBooking findActiveBooking(int roomNumber) throws DaoException {
        return roomBookingDAO.findActiveByRoom(roomNumber)
                .orElseThrow(() -> new ServiceException("No active booking for room " + roomNumber));
    }

    private void addAmenityAndUpdateBooking(RoomBooking booking, Amenity amenity, Date serviceDate)
            throws DaoException {

        AmenityOrder order = new AmenityOrder(
                generateId(),
                booking.getClientId(),
                amenity.getPrice(),
                amenity.getId(),
                serviceDate
        );
        amenityOrderDAO.create(order);

        double newTotal = booking.getTotalPrice() + amenity.getPrice();
        booking.setTotalPrice(newTotal);
        roomBookingDAO.update(booking);
    }
}