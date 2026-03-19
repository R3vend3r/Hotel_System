package hotel_system.service.entityService;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.ServiceException;
import hotel_system.dao.RoomBookingDAO;
import hotel_system.dto.RoomBookingResponse;
import hotel_system.model.entity.Client;
import hotel_system.model.entity.RoomBooking;
import hotel_system.model.mapper.RoomBookingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    private final RoomBookingDAO roomBookingDAO;
    private final RoomBookingMapper roomBookingMapper;

    @Autowired
    public BookingService(RoomBookingDAO roomBookingDAO, RoomBookingMapper roomBookingMapper) {
        this.roomBookingDAO = roomBookingDAO;
        this.roomBookingMapper = roomBookingMapper;
    }

    @Transactional(readOnly = true)
    public Optional<RoomBooking> findActiveBookingEntityByRoom(Integer roomNumber) {
        try {
            return roomBookingDAO.findActiveByRoom(roomNumber);
        } catch (DaoException e) {
            logger.error("Failed to find active booking by room: {}", roomNumber, e);
            throw new ServiceException("Failed to find active booking by room: " + roomNumber, e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<RoomBooking> findActiveBookingEntityByClientId(String clientId) {
        try {
            return roomBookingDAO.findActiveByClientId(clientId);
        } catch (DaoException e) {
            logger.error("Failed to find active booking by client: {}", clientId, e);
            throw new ServiceException("Failed to find active booking by client: " + clientId, e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<Client> findClientByRoom(Integer roomNumber) {
        return findActiveBookingEntityByRoom(roomNumber)
                .map(RoomBooking::getClient);
    }

    @Transactional(readOnly = true)
    public Optional<Integer> findRoomByClientId(String clientId) {
        return findActiveBookingEntityByClientId(clientId)
                .map(booking -> booking.getRoom().getNumber());
    }

    @Transactional(readOnly = true)
    public Optional<RoomBookingResponse> findActiveBookingByRoom(Integer roomNumber) {
        return findActiveBookingEntityByRoom(roomNumber)
                .map(roomBookingMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Optional<RoomBookingResponse> findActiveBookingByClientId(String clientId) {
        return findActiveBookingEntityByClientId(clientId)
                .map(roomBookingMapper::toResponse);
    }
}