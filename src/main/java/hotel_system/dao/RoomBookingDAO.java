package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.Utils.HibernateUtil;
import hotel_system.model.entity.RoomBooking;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class RoomBookingDAO extends HibernateBaseDAO<RoomBooking, String> {

    public RoomBookingDAO() {
        super(RoomBooking.class);
    }


    public Optional<RoomBooking> findActiveByRoom(Integer roomNumber) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<RoomBooking> query = session.createQuery(
                    """
                            FROM RoomBooking rb 
                            WHERE rb.room.number = :roomNumber 
                            AND rb.checkOutDate > CURRENT_TIMESTAMP
                            ORDER BY rb.checkInDate DESC
                            """,
                    RoomBooking.class
            );
            query.setParameter("roomNumber", roomNumber);
            query.setMaxResults(1);
            RoomBooking booking = query.uniqueResult();
            return Optional.ofNullable(booking);
        } catch (Exception e) {
            throw new DaoException("Failed to find active booking by room: " + roomNumber, e);
        }
    }

    public List<RoomBooking> findActiveBookings() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<RoomBooking> query = session.createQuery(
                    """
                            FROM RoomBooking rb 
                            WHERE rb.checkOutDate > CURRENT_TIMESTAMP
                            ORDER BY rb.checkInDate DESC
                            """,
                    RoomBooking.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find active bookings", e);
        }
    }

    public List<RoomBooking> findCompletedBookings() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<RoomBooking> query = session.createQuery(
                    """
                            FROM RoomBooking rb 
                            WHERE rb.checkOutDate <= CURRENT_TIMESTAMP
                            ORDER BY rb.checkOutDate DESC
                            """,
                    RoomBooking.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find completed bookings", e);
        }
    }

    public List<RoomBooking> findByRoom(Integer roomNumber, int limit) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<RoomBooking> query = session.createQuery(
                    """
                            FROM RoomBooking rb 
                            WHERE rb.room.number = :roomNumber 
                            ORDER BY rb.checkOutDate DESC
                            """,
                    RoomBooking.class
            );
            query.setParameter("roomNumber", roomNumber);
            query.setMaxResults(limit);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find bookings by room: " + roomNumber, e);
        }
    }

    public List<RoomBooking> findAllByRoom(Integer roomNumber) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<RoomBooking> query = session.createQuery(
                    """
                            FROM RoomBooking rb 
                            WHERE rb.room.number = :roomNumber 
                            ORDER BY rb.checkOutDate DESC
                            """,
                    RoomBooking.class
            );
            query.setParameter("roomNumber", roomNumber);
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find all bookings by room: " + roomNumber, e);
        }
    }

    public double calculateTotalIncome() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Double> query = session.createQuery(
                    "SELECT COALESCE(SUM(rb.totalPrice), 0) FROM RoomBooking rb",
                    Double.class
            );
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total income", e);
        }
    }

    public double calculateStayCost(int roomNumber, Date endDate) throws DatabaseException {
        Optional<RoomBooking> bookingOpt = findActiveByRoom(roomNumber);
        if (bookingOpt.isEmpty()) {
            return 0.0;
        }

        RoomBooking booking = bookingOpt.get();
        Date startDate = booking.getCheckInDate();

        long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return booking.getTotalPrice() / Math.max(1, days) * days;
    }
}