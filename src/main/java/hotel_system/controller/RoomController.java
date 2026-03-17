package hotel_system.controller;

import hotel_system.Utils.HotelConfig;
import hotel_system.dto.RoomRequest;
import hotel_system.dto.RoomResponse;
import hotel_system.enums.RoomCondition;
import hotel_system.enums.SortType;
import hotel_system.service.entityService.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;
    private final HotelConfig hotelConfig;

    @Autowired
    public RoomController(RoomService roomService, HotelConfig hotelConfig) {
        this.roomService = roomService;
        this.hotelConfig = hotelConfig;
    }

    @GetMapping("/{roomNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Optional<RoomResponse> findRoom(@PathVariable int roomNumber) {
        return roomService.findRoom(roomNumber);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public void addRoom(@RequestBody RoomRequest request) {
        roomService.addRoom(request);
    }

    @PatchMapping("/{roomNumber}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateRoomStatus(
            @PathVariable int roomNumber,
            @RequestParam RoomCondition status) {
        if (hotelConfig.isRoomStatusChangeEnabled()) {
            roomService.updateRoomStatus(roomNumber, status);
        } else {
            throw new IllegalStateException("Изменение статуса комнаты запрещено конфигурацией");
        }
    }

    @PatchMapping("/{roomNumber}/price")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateRoomPrice(
            @PathVariable int roomNumber,
            @RequestParam double newPrice) {
        roomService.updateRoomPrice(roomNumber, newPrice);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomResponse> getRooms(
            @RequestParam(required = false, defaultValue = "NONE") SortType sortType,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable) {
        return onlyAvailable
                ? roomService.getSortedAvailableRooms(sortType)
                : roomService.getSortedRooms(sortType);
    }

    @GetMapping("/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<RoomResponse> getAvailableRoomsByDate(
            @RequestParam @DateTimeFormat(pattern = "yy-MM-dd") Date date) {
        return roomService.getAvailableRoomsByDate(date);
    }

    @GetMapping("/available/count")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public int getAvailableRoomsCount() {
        return roomService.countAvailableRooms();
    }

    @GetMapping("/{roomNumber}/available")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public boolean isRoomAvailable(@PathVariable int roomNumber) {
        return roomService.isRoomAvailable(roomNumber);
    }
}