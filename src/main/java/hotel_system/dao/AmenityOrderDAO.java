package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.AmenityOrder;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AmenityOrderDAO extends HibernateBaseDAO<AmenityOrder, String> {

    public AmenityOrderDAO() {
        super(AmenityOrder.class);
    }

    public double calculateTotalForRoom(Integer roomNumber) throws DatabaseException {
        try {
            TypedQuery<Double> query = entityManager.createQuery(
                    """
                            SELECT COALESCE(SUM(ao.totalPrice), 0)
                            FROM AmenityOrder ao
                            WHERE ao.clientId IN (
                                SELECT rb.client.id
                                FROM RoomBooking rb
                                WHERE rb.room.number = :roomNumber
                                AND rb.checkOutDate > CURRENT_TIMESTAMP
                            )
                            """,
                    Double.class
            );
            query.setParameter("roomNumber", roomNumber);

            Double result = query.getSingleResult();
            return result != null ? result : 0.0;

        } catch (NoResultException e) {
            return 0.0;
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total for room: " + roomNumber, e);
        }
    }

    public double calculateTotalIncome() throws DatabaseException {
        try {
            TypedQuery<Double> query = entityManager.createQuery(
                    "SELECT COALESCE(SUM(totalPrice), 0) FROM AmenityOrder",
                    Double.class
            );
            return query.getSingleResult();
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total income from amenity orders", e);
        }
    }

    public List<AmenityOrder> findClientId(String clientId){
        try{
            return entityManager.createQuery(
                    "FROM AmenityOrder ao WHERE ao.clientId = :clientId " +
                            "ORDER BY ao.serviceDate DESC",
                    AmenityOrder.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        } catch (Exception e){
            throw new DaoException("Failed to find amenity orders by clientId: " + clientId, e);
        }
    }
}