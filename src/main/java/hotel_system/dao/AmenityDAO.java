package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.Utils.HibernateUtil;
import hotel_system.model.entity.Amenity;
import jakarta.persistence.TypedQuery;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AmenityDAO extends HibernateBaseDAO<Amenity, String> {

    public AmenityDAO() {
        super(Amenity.class);
    }

    public Optional<Amenity> findByName(String name) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Amenity> query = session.createQuery(
                    "FROM Amenity a WHERE LOWER(a.name) = LOWER(:name)",
                    Amenity.class
            );
            query.setParameter("name", name);
            Amenity amenity = query.uniqueResult();
            return Optional.ofNullable(amenity);
        } catch (Exception e) {
            throw new DaoException("Failed to find amenity by name: " + name, e);
        }
    }

    public List<Amenity> findAllSortedByPrice() throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Amenity> query = session.createQuery(
                    "FROM Amenity a ORDER BY a.price",
                    Amenity.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find amenities sorted by price", e);
        }
    }
}