package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.Utils.HibernateUtil;
import hotel_system.model.entity.Room;
import hotel_system.enums.RoomCondition;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class RoomDAO extends HibernateBaseDAO<Room, Integer> {

    public RoomDAO() {
        super(Room.class);
    }


    public boolean isRoomAvailable(int number) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Boolean> query = session.createQuery(
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
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Room room = session.createQuery(
                                "FROM Room r WHERE r.number = :number", Room.class)
                        .setParameter("number", number)
                        .uniqueResult();

                if (room != null) {
                    room.setRoomCondition(status);
                    session.merge(room);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        } catch (Exception e) {
            throw new DaoException("Failed to update room status: " + number, e);
        }
    }

    public void updateRoomPrice(int number, double newPrice) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Room room = session.createQuery(
                                "FROM Room r WHERE r.number = :number", Room.class)
                        .setParameter("number", number)
                        .uniqueResult();

                if (room != null) {
                    room.setPriceForDay(newPrice);
                    session.merge(room);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }
        } catch (Exception e) {
            throw new DaoException("Failed to update room price: " + number, e);
        }
    }

    public int countAvailableRooms() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Long> query = session.createQuery(
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