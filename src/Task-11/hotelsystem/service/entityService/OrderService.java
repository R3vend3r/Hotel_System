package hotelsystem.service.entityService;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.comparator.AmenityComparator.DateAmenComparator;
import hotelsystem.comparator.AmenityComparator.PriceAmenComparator;
import hotelsystem.comparator.OrderCorparator.AlphabetComparator;
import hotelsystem.comparator.OrderCorparator.DateComparator;
import hotelsystem.comparator.OrderCorparator.NoneComparator;
import hotelsystem.dao.AmenityOrderDAO;
import hotelsystem.dao.RoomBookingDAO;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.enums.SortType;
import hotelsystem.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.*;

@Component
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Inject private RoomBookingDAO roomBookingDAO;
    @Inject private AmenityOrderDAO amenityOrderDAO;

    public void createRoomBooking(Client client, Room room, Date checkOutDate) {
        try {
            DatabaseManager.getInstance().beginTransaction();
            RoomBooking booking = createBookingObject(client, room, checkOutDate);
            roomBookingDAO.create(booking);
            DatabaseManager.getInstance().commit();
            logger.info("Бронирование создано для клиента {} в комнате {}",
                    client.getId(), room.getNumberRoom());
        } catch (SQLException e) {
            DatabaseManager.getInstance().rollback();
            logger.error("Failed to create booking", e);
            throw new RuntimeException("Failed to create booking", e);
        }
    }

    public void addAmenityToBooking(int roomNumber, Amenity amenity, Date serviceDate) {
        try {
            DatabaseManager.getInstance().beginTransaction();
            RoomBooking booking = findActiveBooking(roomNumber);
            addAmenityAndUpdateBooking(booking, amenity, serviceDate);
            DatabaseManager.getInstance().commit();
            logger.info("Услуга '{}' добавлена к бронированию в комнате {}",
                    amenity.getName(), roomNumber);
        } catch (SQLException e) {
            DatabaseManager.getInstance().rollback();
            logger.error("Failed to add amenity to booking", e);
            throw new RuntimeException("Failed to add amenity to booking", e);
        }
    }

    public void evictClient(int roomNumber) {
        try {
            DatabaseManager.getInstance().beginTransaction();
            findAndUpdateBooking(roomNumber);
            DatabaseManager.getInstance().commit();
            logger.info("Клиент выселен из комнаты {}", roomNumber);
        } catch (SQLException e) {
            DatabaseManager.getInstance().rollback();
            logger.error("Failed to evict client", e);
            throw new RuntimeException("Failed to evict client", e);
        }
    }

    public Optional<RoomBooking> getActiveBookingByRoom(int roomNumber) {
        try {
            return roomBookingDAO.findActiveByRoom(roomNumber);
        } catch (SQLException e) {
            logger.error("Failed to get active booking", e);
            throw new RuntimeException("Failed to get active booking", e);
        }
    }

    public double calculateRoomPayment(int roomNumber) {
        try {
            double stayCost = roomBookingDAO.calculateStayCost(roomNumber, new Date());
            double amenityCost = amenityOrderDAO.calculateTotalForRoom(roomNumber);

            return stayCost + amenityCost;
        } catch (SQLException e) {
            logger.error("Failed to calculate room payment", e);
            throw new RuntimeException("Failed to calculate room payment", e);
        }
    }

    public double calculateTotalRevenue() {
        try {
            double bookingIncome = roomBookingDAO.calculateTotalIncome();
            double amenityIncome = amenityOrderDAO.calculateTotalIncome();
            return bookingIncome + amenityIncome;
        } catch (SQLException e) {
            logger.error("Failed to calculate total revenue", e);
            throw new RuntimeException("Failed to calculate total revenue", e);
        }
    }

    public List<RoomBooking> getActiveBookingsSorted(SortType sortType) {
        try {
            List<RoomBooking> bookings = roomBookingDAO.findActiveBookings();
            return sortBookings(bookings, sortType);
        } catch (SQLException e) {
            logger.error("Failed to get active bookings", e);
            throw new RuntimeException("Failed to get active bookings", e);
        }
    }

    public List<RoomBooking> getCompletedBookings() {
        try {
            return roomBookingDAO.findCompletedBookings();
        } catch (SQLException e) {
            logger.error("Failed to get completed bookings", e);
            throw new RuntimeException("Failed to get completed bookings", e);
        }
    }

    public List<AmenityOrder> getAmenityOrdersSorted(SortType sortType) {
        try {
            List<AmenityOrder> orders = amenityOrderDAO.findAll();
            return sortAmenityOrders(orders, sortType);
        } catch (SQLException e) {
            logger.error("Failed to get amenity orders", e);
            throw new RuntimeException("Failed to get amenity orders", e);
        }
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findByRoom(roomNumber, 3);
        } catch (SQLException e) {
            logger.error("Failed to get last three bookings for room", e);
            throw new RuntimeException("Failed to get last three bookings for room", e);
        }
    }

    public List<RoomBooking> getAllBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findAllByRoom(roomNumber);
        } catch (SQLException e) {
            logger.error("Failed to get all bookings for room {}", roomNumber, e);
            throw new RuntimeException("Failed to get all bookings for room " + roomNumber, e);
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


    private void findAndUpdateBooking(int roomNumber) throws SQLException {
        Optional<RoomBooking> bookingOpt = roomBookingDAO.findActiveByRoom(roomNumber);
        if (bookingOpt.isEmpty()) {
            throw new RuntimeException("No active booking for room " + roomNumber);
        }

        RoomBooking booking = bookingOpt.get();
        Date checkOutDate = new Date(System.currentTimeMillis() + 1000);
        booking.setCheckOutDate(checkOutDate);
        roomBookingDAO.update(booking);
    }

    private RoomBooking findActiveBooking(int roomNumber) throws SQLException {
        return roomBookingDAO.findActiveByRoom(roomNumber)
                .orElseThrow(() -> new RuntimeException("No active booking for room " + roomNumber));
    }

    private void addAmenityAndUpdateBooking(RoomBooking booking, Amenity amenity, Date serviceDate)
            throws SQLException {

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