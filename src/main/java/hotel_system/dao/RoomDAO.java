package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.Room;
import hotel_system.enums.RoomCondition;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO extends HibernateBaseDAO<Room, Integer> {

    public RoomDAO(SessionFactory sessionFactory) {
        super(Room.class, sessionFactory);
    }


    public boolean isRoomAvailable(int number) throws DatabaseException {
        try {
            Query<Boolean> query = getCurrentSession().createQuery(
                    "SELECT r.isAvailable FROM Room r WHERE r.number = :number",
                    Boolean.class
            );
            query.setParameter("number", number);
            Boolean result = query.uniqueResult();
            return result != null && result;
        } catch (Exception e) {
            throw new DaoException("Failed to check if room is available: " + number, e);
        }
    }

    public void updateRoomStatus(int number, RoomCondition status) throws DatabaseException {
            try {
                Room room = getCurrentSession().createQuery(
                                "FROM Room r WHERE r.number = :number", Room.class)
                        .setParameter("number", number)
                        .uniqueResult();

                if (room != null) {
                    room.setRoomCondition(status);
                    getCurrentSession().merge(room);
                }
        } catch (Exception e) {
            throw new DaoException("Failed to update room status: " + number, e);
        }
    }

    public void updateRoomPrice(int number, double newPrice) throws DatabaseException {
            try {
                Room room = getCurrentSession().createQuery(
                                "FROM Room r WHERE r.number = :number", Room.class)
                        .setParameter("number", number)
                        .uniqueResult();

                if (room != null) {
                    room.setPriceForDay(newPrice);
                    getCurrentSession().merge(room);
                }
        } catch (Exception e) {
            throw new DaoException("Failed to update room price: " + number, e);
        }
    }

    public int countAvailableRooms() throws DatabaseException {
        try {
            Query<Long> query = getCurrentSession().createQuery(
                    "SELECT COUNT(r) FROM Room r WHERE r.isAvailable = true",
                    Long.class
            );
            Long count = query.uniqueResult();
            return count != null ? count.intValue() : 0;
        } catch (Exception e) {
            throw new DaoException("Failed to count available rooms", e);
        }
    }
}