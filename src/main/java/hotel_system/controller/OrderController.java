package hotel_system.controller;

import hotel_system.Exception.ManagerHotelException;
import hotel_system.enums.SortType;
import hotel_system.model.entity.*;
import hotel_system.service.entityService.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public void addAmenityToClient(int roomNumber, Amenity amenity, Date serviceDate) {
        orderService.addAmenityToBooking(roomNumber, amenity, serviceDate);
    }

    public void settleClient(Client client, Room room, Date checkOutDate) {
        try {
            orderService.createRoomBooking(client, room, checkOutDate);
            logger.info("Клиент {} заселен в комнату {}", client.getId(), room.getNumber());
        } catch (Exception e) {
            logger.error("Ошибка при заселении клиента", e);
            throw new ManagerHotelException("Ошибка при заселении клиента: " + e.getMessage(), e);
        }
    }

    public void evictClient(Integer roomNumber){
        orderService.evictClient(roomNumber);
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

    public double calculateRoomPayment(int roomNumber) {
        return orderService.calculateRoomPayment(roomNumber);
    }

    public double calculateTotalRevenue() {
        return orderService.calculateTotalRevenue();
    }

    public List<Client> getRoomHistory(int roomNumber) {
        return orderService.getRoomHistory(roomNumber);
    }
}
