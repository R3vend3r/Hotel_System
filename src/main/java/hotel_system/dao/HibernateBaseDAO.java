package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Utils.HibernateUtil;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public abstract class HibernateBaseDAO<T, ID> implements GenericDAO<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateBaseDAO.class);
    private final Class<T> type;

    protected HibernateBaseDAO(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T entity) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            logger.debug("Created entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error creating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to create " + type.getSimpleName(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try (Session session = HibernateUtil.getSession()) {
            T entity = session.get(type, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            logger.error("Error finding {}", type.getSimpleName(), e);
            throw new DaoException("Failed to find " + type.getSimpleName(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try (Session session = HibernateUtil.getSession()) {
            return session.createQuery("FROM " + type.getSimpleName(), type)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all {}", type.getSimpleName(), e);
            throw new DaoException("Failed to getAll " + type.getSimpleName(), e);
        }
    }

    @Override
    public void update(T entity) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            logger.debug("Updated entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error updating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to update " + type.getSimpleName(), e);
        }
    }

    @Override
    public void delete(ID id) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            T entity = session.get(type, id);
            if (entity != null) {
                session.remove(entity);
                session.getTransaction().commit();
                logger.debug("Deleted {} with id: {}", type.getSimpleName(), id);
            } else {
                session.getTransaction().rollback();
                logger.debug("No {} found with id: {}", type.getSimpleName(), id);
            }
        } catch (Exception e) {
            logger.error("Error deleting {}", type.getSimpleName(), e);
            throw new DaoException("Failed to delete " + type.getSimpleName(), e);
        }
    }
}