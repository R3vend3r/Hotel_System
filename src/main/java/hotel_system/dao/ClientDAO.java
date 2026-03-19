package hotel_system.dao;

import hotel_system.Exception.DaoException;
import hotel_system.Exception.DatabaseException;
import hotel_system.model.entity.Client;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ClientDAO extends HibernateBaseDAO<Client, String> {

    public ClientDAO() {
        super(Client.class);
    }

    public Optional<Client> findByNameAndSurname(String name, String surname) throws DatabaseException {
        try {
            TypedQuery<Client> query = entityManager.createQuery(
                    "FROM Client c WHERE LOWER(c.name) = LOWER(:name) AND LOWER(c.surname) = LOWER(:surname)",
                    Client.class
            );
            query.setParameter("name", name);
            query.setParameter("surname", surname);

            Client client = query.getSingleResult();
            return Optional.ofNullable(client);
        } catch (Exception e) {
            throw new DaoException("Failed to find client by name and surname: " + name + " " + surname, e);
        }
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(c) FROM Client c", Long.class)
                .getSingleResult();
    }
}