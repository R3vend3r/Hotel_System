package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.Utils.HibernateUtil;
import hotel_system.model.entity.Client;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientDAO extends HibernateBaseDAO<Client, String> {

    public ClientDAO() {
        super(Client.class);
    }

    public Optional<Client> findByRoomNumber(Integer roomNumber) throws DatabaseException {
        try (Session session = HibernateUtil.getSession()) {
            Query<Client> query = session.createQuery(
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
}