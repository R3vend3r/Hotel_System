package hotel_system.controller;

import hotel_system.dto.*;
import hotel_system.dto.DtoMethod.AddAmenityRequest;
import hotel_system.dto.DtoMethod.SettleClientRequest;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void addAmenityToClient(@RequestBody AddAmenityRequest request) {
        orderService.addAmenityToBooking(request);
    }

    @PostMapping("/settle")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void settleClient(@RequestBody SettleClientRequest request) {
        orderService.settleClient(request);
    }

    @PostMapping("/evict/{roomNumber}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void evictClient(@PathVariable Integer roomNumber) {
        orderService.evictClient(roomNumber);
    }

    @GetMapping("/bookings/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomBookingResponse> getAllActiveBookings(
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return orderService.getActiveBookingsSorted(sortType);
    }

    @GetMapping("/bookings/completed")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomBookingResponse> getAllCompletedBookings() {
        return orderService.getCompletedBookings();
    }

    @GetMapping("/amenities/client/{clientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<AmenityOrderResponse> getClientAmenitiesSorted(
            @PathVariable String clientId,
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType) {
        return orderService.getClientAmenitiesSorted(clientId, sortType);
    }

    @GetMapping("/bookings/room/{roomNumber}/last")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomBookingResponse> getLastThreeBookingsForRoom(@PathVariable int roomNumber) {
        return orderService.getLastThreeBookingsForRoom(roomNumber);
    }

    @GetMapping("/payment/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public double calculateRoomPayment(@PathVariable int roomNumber) {
        return orderService.calculateRoomPayment(roomNumber);
    }

    @GetMapping("/revenue/total")
    @PreAuthorize("hasRole('ADMIN')")
    public double calculateTotalRevenue() {
        return orderService.calculateTotalRevenue();
    }

    @GetMapping("/history/room/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<ClientResponse> getRoomHistory(@PathVariable int roomNumber) {
        return orderService.getRoomHistory(roomNumber);
    }
}