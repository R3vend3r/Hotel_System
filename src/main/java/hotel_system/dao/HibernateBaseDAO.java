package hotel_system.dao;

import hotel_system.Exception.DaoException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public abstract class HibernateBaseDAO<T, ID> implements GenericDAO<T, ID> {
    private static final Logger logger = LoggerFactory.getLogger(HibernateBaseDAO.class);
    private final Class<T> type;

    @PersistenceContext
    protected EntityManager entityManager;

    protected HibernateBaseDAO(Class<T> type) {
        this.type = type;
    }

    @Override
    public void create(T entity) {
        try{
            entityManager.persist(entity);
            logger.debug("Created entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error creating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to create " + type.getSimpleName(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        try {
            T entity = entityManager.find(type, id);
            return Optional.ofNullable(entity);
        } catch (Exception e) {
            logger.error("Error finding {}", type.getSimpleName(), e);
            throw new DaoException("Failed to find " + type.getSimpleName(), e);
        }
    }

    @Override
    public List<T> findAll() {
        try {
            return entityManager.createQuery("FROM " + type.getSimpleName(), type)
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
            entityManager.merge(entity);
            logger.debug("Updated entity: {}", entity);
        } catch (Exception e) {
            logger.error("Error updating {}", type.getSimpleName(), e);
            throw new DaoException("Failed to update " + type.getSimpleName(), e);
        }
    }

    @Override
    public void delete(ID id) {
        try {
            T entity = entityManager.find(type, id);
            if (entity != null) {
                entityManager.remove(entity);
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