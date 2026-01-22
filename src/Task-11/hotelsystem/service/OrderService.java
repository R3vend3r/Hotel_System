package hotelsystem.service;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.dependencies.annotation.Component;
import hotelsystem.dependencies.annotation.Inject;
import hotelsystem.enums.SortType;
import hotelsystem.interfaceClass.IClearable;
import hotelsystem.interfaceClass.IOrderRepository;
import hotelsystem.model.*;
import hotelsystem.repo.dao.AmenityOrderDAO;
import hotelsystem.repo.dao.RoomBookingDAO;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderService implements IClearable {
    @Inject
    private IOrderRepository orderRepository;

    @Inject
    private RoomBookingDAO roomBookingDAO;

    @Inject
    private AmenityOrderDAO amenityOrderDAO;

    public void createRoomBooking(Client client, Room room, Date checkInDate, Date checkOutDate) {
        try {
            RoomBooking booking = new RoomBooking(
                    generateId(),
                    client,
                    room,
                    calculateStayCost(room.getPriceForDay(), checkInDate, checkOutDate),
                    checkInDate,
                    checkOutDate
            );

            roomBookingDAO.create(booking);
            orderRepository.addRoomBooking(booking);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to create booking", e);
        }
    }

    public void addAmenityToBooking(int roomNumber, Amenity amenity, Date serviceDate) {
        try {
            RoomBooking booking = getActiveBookingByRoom(roomNumber);

            AmenityOrder order = new AmenityOrder(
                    generateId(),
                    booking.getClientId(),
                    amenity.getPrice(),
                    amenity.getId(),
                    serviceDate
            );

            amenityOrderDAO.create(order);
            orderRepository.addAmenityOrder(order);
            booking.addService(order);
            roomBookingDAO.update(booking);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to add amenity", e);
        }
    }

//    public void completeRoomBooking(int roomNumber, Date checkOutDate) {
//        try {
//            RoomBooking booking = getActiveBookingByRoom(roomNumber);
//            booking.setCheckOutDate(checkOutDate);
//            roomBookingDAO.update(booking);
//            orderRepository.completeRoomBooking(booking);
//        } catch (DatabaseException e) {
//            throw new RuntimeException("Failed to complete booking", e);
//        }
//    }

    public RoomBooking getActiveBookingByRoom(int roomNumber) {
        try {
            Optional<RoomBooking> booking = orderRepository.findActiveBookingByRoom(roomNumber);
            if (booking.isEmpty()) {
                booking = roomBookingDAO.findAll().stream()
                        .filter(b -> b.getRoom().getNumberRoom() == roomNumber &&
                                b.getCheckOutDate().after(new Date()))
                        .findFirst();
                booking.ifPresent(orderRepository::addRoomBooking);
            }
            return booking.orElseThrow(() ->
                    new IllegalArgumentException("No active booking for room " + roomNumber));
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to find booking", e);
        }
    }

    public List<RoomBooking> getCompletedBookings() {
        try {
            List<RoomBooking> bookings = roomBookingDAO.findAll().stream()
                    .filter(b -> b.getCheckOutDate().after(new Date()))
                    .toList();
            bookings.forEach(orderRepository::addRoomBooking);
            return orderRepository.getCompletedBookings();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get active bookings", e);
        }
    }

    public List<AmenityOrder> getAllAmenityOrders() {
        try {
            List<AmenityOrder> orders = amenityOrderDAO.findAll();
            orders.forEach(orderRepository::addAmenityOrder);
            return orderRepository.getAmenityOrders();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get amenity orders", e);
        }
    }
    public List<AmenityOrder> getAmenitiesForBooking(String bookingId) {
        try {
            amenityOrderDAO.findAll().forEach(orderRepository::addAmenityOrder);
            return orderRepository.getAmenityOrdersForBooking(bookingId);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get amenity orders", e);
        }
    }

    public double calculateAmenityCost(int roomNumber) {
        try {
            return orderRepository.calculateAmenityCost(roomNumber);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to calculate amenity cost", e);
        }
    }

    public List<RoomBooking> getActiveBookingsSorted(SortType sortType) {
        try {
            roomBookingDAO.findAll().stream()
                    .filter(b -> b.getCheckOutDate().after(new Date()))
                    .forEach(orderRepository::addRoomBooking);
            return orderRepository.getSortedBookings(sortType);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get sorted bookings", e);
        }
    }

    public List<AmenityOrder> getAmenityOrdersSorted(SortType sortType) {
        try {
            amenityOrderDAO.findAll().forEach(orderRepository::addAmenityOrder);
            return orderRepository.getSortedAmenityOrders(sortType);
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get sorted amenity orders", e);
        }
    }

    public List<RoomBooking> getLastThreeBookingsForRoom(int roomNumber) {
        try {
            return roomBookingDAO.findAll().stream()
                    .filter(b -> b.getRoom().getNumberRoom() == roomNumber)
                    .sorted(Comparator.comparing(RoomBooking::getCheckOutDate).reversed())
                    .limit(3)
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to get last bookings", e);
        }
    }

    @Override
    public void clear() {
        try {
            roomBookingDAO.findAll().forEach(b -> {
                try {
                    roomBookingDAO.delete(b.getId());
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            });
            amenityOrderDAO.findAll().forEach(o -> {
                try {
                    amenityOrderDAO.delete(o.getId());
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            });
            orderRepository.clearAll();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to clear orders", e);
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    private double calculateStayCost(double pricePerDay, Date start, Date end) {
        long days = (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
        return pricePerDay * Math.max(1, days);
    }

    public double calculatingTotalIncome() {
        try {
            roomBookingDAO.findAll().forEach(orderRepository::addRoomBooking);
            amenityOrderDAO.findAll().forEach(orderRepository::addAmenityOrder);
            return orderRepository.calculatingTotalIncome();
        } catch (DatabaseException e) {
            throw new RuntimeException("Failed to calculate revenue", e);
        }
    }
}