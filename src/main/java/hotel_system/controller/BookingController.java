package hotel_system.controller;

import hotel_system.dto.ClientResponse;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.service.entityService.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/active/room/{roomNumber}")
    public ResponseEntity<RoomBookingResponse> getActiveBookingByRoom(@PathVariable Integer roomNumber) {
        Optional<RoomBookingResponse> booking = bookingService.findActiveBookingByRoom(roomNumber);
        return booking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active/client/{clientId}")
    public ResponseEntity<RoomBookingResponse> getActiveBookingByClientId(@PathVariable String clientId) {
        Optional<RoomBookingResponse> booking = bookingService.findActiveBookingByClientId(clientId);
        return booking.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client/by-room/{roomNumber}")
    public ResponseEntity<ClientResponse> getClientByRoom(@PathVariable Integer roomNumber) {
        return bookingService.findClientByRoom(roomNumber)
                .map(client -> ResponseEntity.ok(new ClientResponse(
                        client.getId(),
                        client.getName(),
                        client.getSurname()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/room/by-client/{clientId}")
    public ResponseEntity<Integer> getRoomByClientId(@PathVariable String clientId) {
        return bookingService.findRoomByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}