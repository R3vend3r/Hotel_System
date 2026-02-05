package hotelsystem.dao;

import hotelsystem.Exception.DaoException;
import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.entity.Amenity;
import hotelsystem.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

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