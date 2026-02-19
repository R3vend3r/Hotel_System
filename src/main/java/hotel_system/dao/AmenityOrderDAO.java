package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.Utils.HibernateUtil;
import hotel_system.model.entity.AmenityOrder;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AmenityOrderDAO extends HibernateBaseDAO<AmenityOrder, String> {

    public AmenityOrderDAO() {
        super(AmenityOrder.class);
    }

    public double calculateTotalForRoom(Integer roomNumber) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Double> query = session.createQuery(
                    """
                            SELECT COALESCE(SUM(ao.totalPrice), 0) 
                            FROM AmenityOrder ao 
                            JOIN Client c ON ao.clientId = c.id 
                            WHERE c.roomNumber = :roomNumber
                            """,
                    Double.class
            );
            query.setParameter("roomNumber", roomNumber);
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total for room: " + roomNumber, e);
        }
    }

    public double calculateTotalIncome() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Double> query = session.createQuery(
                    "SELECT COALESCE(SUM(totalPrice), 0) FROM AmenityOrder",
                    Double.class
            );
            return query.uniqueResult();
        } catch (Exception e) {
            throw new DaoException("Failed to calculate total income from amenity orders", e);
        }
    }
}