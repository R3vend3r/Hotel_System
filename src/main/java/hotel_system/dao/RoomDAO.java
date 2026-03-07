package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.Room;
import hotel_system.enums.RoomCondition;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO extends HibernateBaseDAO<Room, Integer> {

    public RoomDAO() {
        super(Room.class);
    }


    public boolean isRoomAvailable(int number) throws DatabaseException {
        try {
            TypedQuery<Boolean> query = entityManager.createQuery(
                    "SELECT r.isAvailable FROM Room r WHERE r.number = :number",
                    Boolean.class
            );
            query.setParameter("number", number);
            Boolean result = query.getSingleResult();
            return result != null && result;
        } catch (Exception e) {
            throw new DaoException("Failed to check if room is available: " + number, e);
        }
    }

    public void updateRoomStatus(int number, RoomCondition status) throws DatabaseException {
            try {
                Room room = entityManager.createQuery(
                                "FROM Room r WHERE r.number = :number", Room.class)
                        .setParameter("number", number)
                        .getSingleResult();

                if (room != null) {
                    room.setRoomCondition(status);
                    entityManager.merge(room);
                }
        } catch (Exception e) {
            throw new DaoException("Failed to update room status: " + number, e);
        }
    }

    public void updateRoomPrice(int number, double newPrice) throws DatabaseException {
        try {
            Room room = entityManager.createQuery(
                            "FROM Room r WHERE r.number = :number", Room.class)
                    .setParameter("number", number)
                    .getSingleResult();

            if (room == null) {
                throw new DaoException("Room not found with number: " + number);
            }

            room.setPriceForDay(newPrice);
            entityManager.merge(room);

        } catch (Exception e) {
            throw new DaoException("Failed to update room price: " + number, e);
        }
    }

    public int countAvailableRooms() throws DatabaseException {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(r) FROM Room r WHERE r.isAvailable = true",
                    Long.class
            );
            Long count = query.getSingleResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            throw new DaoException("Failed to count available rooms", e);
        }
    }
}