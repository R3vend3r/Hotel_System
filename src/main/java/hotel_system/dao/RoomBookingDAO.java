package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.RoomBooking;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomBookingDAO extends HibernateBaseDAO<RoomBooking, String> {
    public RoomBookingDAO() {
        super(RoomBooking.class);
    }


    public Optional<RoomBooking> findActiveByClientId(String clientId) throws DatabaseException {
        try {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb 
                            JOIN FETCH rb.room
                            WHERE rb.client.id = :clientId 
                            AND rb.checkOutDate > CURRENT_TIMESTAMP
                            ORDER BY rb.checkInDate DESC
                            """,
                    RoomBooking.class
            );
            query.setParameter("clientId", clientId);
            query.setMaxResults(1);
            RoomBooking booking = query.getSingleResult();
            return Optional.ofNullable(booking);
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DaoException("Failed to find active booking by client id: " + clientId, e);
        }
    }


    public Optional<RoomBooking> findActiveByRoom(Integer roomNumber) throws DatabaseException {
        try {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb 
                            JOIN FETCH rb.client
                            WHERE rb.room.number = :roomNumber 
                            AND rb.checkOutDate > CURRENT_TIMESTAMP
                            ORDER BY rb.checkInDate DESC
                            """,
                    RoomBooking.class
            );
            query.setParameter("roomNumber", roomNumber);
            query.setMaxResults(1);
            return Optional.ofNullable(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new DaoException("Failed to find active booking by room: " + roomNumber, e);
        }
    }

    public List<RoomBooking> findActiveBookings() throws DatabaseException {
        try {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb
                            JOIN FETCH rb.client 
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
        try {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb\s
                            JOIN FETCH rb.client\s
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
        try  {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb 
                            JOIN FETCH rb.client                
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
        try {
            TypedQuery<RoomBooking> query = entityManager.createQuery(
                    """
                            FROM RoomBooking rb 
                            JOIN FETCH rb.client
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
        try  {
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT COALESCE(SUM(rb.totalPrice), 0) FROM RoomBooking rb",
                    Double.class
            );
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total income", e);
        }
    }

    public double calculateStayCost(int roomNumber) throws DatabaseException {
        try {
            String hql = """
                SELECT rb.totalPrice 
                FROM RoomBooking rb 
                WHERE rb.room.number = :roomNumber 
                AND rb.checkOutDate > CURRENT_TIMESTAMP
                ORDER BY rb.checkInDate DESC
                """;

            TypedQuery<Double> query = entityManager.createQuery(hql, Double.class)
                    .setParameter("roomNumber", roomNumber)
                    .setMaxResults(1);

            Double result = query.getSingleResult();
            return result != null ? result : 0.0;

        } catch (Exception e) {
            throw new DaoException("Failed to calculate stay cost for room: " + roomNumber, e);
        }
    }
}