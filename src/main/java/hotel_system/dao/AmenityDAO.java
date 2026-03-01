package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.Amenity;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AmenityDAO extends HibernateBaseDAO<Amenity, String> {

    public AmenityDAO(SessionFactory sessionFactory) {
        super(Amenity.class, sessionFactory);
    }

    public Optional<Amenity> findByName(String name) throws DatabaseException {
        try {
            Query<Amenity> query = getCurrentSession().createQuery(
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
        try {
            Query<Amenity> query = getCurrentSession().createQuery(
                    "FROM Amenity a ORDER BY a.price",
                    Amenity.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find amenities sorted by price", e);
        }
    }

    public List<Amenity> findAllSortedByName() throws DatabaseException {
        try {
            Query<Amenity> query = getCurrentSession().createQuery(
                    "FROM Amenity a ORDER BY a.name",
                    Amenity.class
            );
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException("Failed to find amenities sorted by name", e);
        }
    }
}