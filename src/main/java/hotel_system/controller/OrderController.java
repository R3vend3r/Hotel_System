package hotel_system.controller;

import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/amenities")
    @ResponseStatus(HttpStatus.CREATED)
    public void addAmenityToClient(@RequestBody AddAmenityRequest request) {
        orderService.addAmenityToBooking(request);
    }

    @PostMapping("/settle")
    @ResponseStatus(HttpStatus.CREATED)
    public void settleClient(@RequestBody SettleClientRequest request) {
        orderService.settleClient(request);
    }

    @PostMapping("/evict/{roomNumber}")
    public void evictClient(@PathVariable Integer roomNumber) {
        orderService.evictClient(roomNumber);
    }

    @GetMapping("/bookings/active")
    public List<RoomBookingResponse> getAllActiveBookings(
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return orderService.getActiveBookingsSorted(sortType);
    }

    @GetMapping("/bookings/completed")
    public List<RoomBookingResponse> getAllCompletedBookings() {
        return orderService.getCompletedBookings();
    }

    @GetMapping("/amenities/client/{clientId}")
    public List<AmenityOrderResponse> getClientAmenitiesSorted(
            @PathVariable String clientId,
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return orderService.getClientAmenitiesSorted(clientId, sortType);
    }

    @GetMapping("/bookings/room/{roomNumber}/last")
    public List<RoomBookingResponse> getLastThreeBookingsForRoom(@PathVariable int roomNumber) {
        return orderService.getLastThreeBookingsForRoom(roomNumber);
    }

    @GetMapping("/payment/room/{roomNumber}")
    public double calculateRoomPayment(@PathVariable int roomNumber) {
        return orderService.calculateRoomPayment(roomNumber);
    }

    @GetMapping("/revenue/total")
    public double calculateTotalRevenue() {
        return orderService.calculateTotalRevenue();
    }

    @GetMapping("/history/room/{roomNumber}")
    public List<ClientResponse> getRoomHistory(@PathVariable int roomNumber) {
        return orderService.getRoomHistory(roomNumber);
    }
}