package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.Client;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientDAO extends HibernateBaseDAO<Client, String> {

    public ClientDAO(SessionFactory sessionFactory) {
        super(Client.class, sessionFactory);
    }

    public Optional<Client> findByRoomNumber(Integer roomNumber) throws DatabaseException {
        try {
            Query<Client> query = getCurrentSession().createQuery(
                    "FROM Client c WHERE c.roomNumber = :roomNumber",
                    Client.class
            );
            query.setParameter("roomNumber", roomNumber);
            Client client = query.uniqueResult();
            return Optional.ofNullable(client);
        } catch (Exception e) {
            throw new DaoException("Failed to find client by room number: " + roomNumber, e);
        }
    }

    public long count() {
        try {
            return getCurrentSession().createQuery("SELECT COUNT(c) FROM Client c", Long.class)
                    .uniqueResult();
        } finally {

        }
    }
}