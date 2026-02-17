package hotelsystem.dao;

import hotelsystem.Exception.DaoException;
import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.entity.Client;
import hotelsystem.Utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

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