package hotel_system.dao;

import hotel_system.Exception.DaoException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class HibernateBaseDAO<T, ID> implements GenericDAO<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateBaseDAO.class);
    private final Class<T> type;
    private final SessionFactory sessionFactory;

    protected HibernateBaseDAO(Class<T> type, SessionFactory sessionFactory) {
        this.type = type;
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Override
    public void create(T entity) {
        try{
            getCurrentSession().persist(entity);
            logger.debug("Created entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error creating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to create " + type.getSimpleName(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            T entity = getCurrentSession().get(type, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            logger.error("Error finding {}", type.getSimpleName(), e);
            throw new DaoException("Failed to find " + type.getSimpleName(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return getCurrentSession().createQuery("FROM " + type.getSimpleName(), type)
                    .getResultList();
        } catch (Exception e) {
            logger.error("Error getting all {}", type.getSimpleName(), e);
            throw new DaoException("Failed to getAll " + type.getSimpleName(), e);
        }
    }

    @Override
    public void update(T entity) {
        try {
            if (entity == null) {
                throw new IllegalArgumentException("Entity cannot be null");
            }
            getCurrentSession().merge(entity);
            logger.debug("Updated entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error updating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to update " + type.getSimpleName(), e);
        }
    }

    @Override
    public void delete(ID id) {
        try {
            T entity = getCurrentSession().get(type, id);
            if (entity != null) {
                getCurrentSession().remove(entity);
                logger.debug("Deleted {} with id: {}", type.getSimpleName(), id);
            } else {
                logger.debug("No {} found with id: {}", type.getSimpleName(), id);
            }
        } catch (Exception e) {
            logger.error("Error deleting {}", type.getSimpleName(), e);
            throw new DaoException("Failed to delete " + type.getSimpleName(), e);
        }
    }
}