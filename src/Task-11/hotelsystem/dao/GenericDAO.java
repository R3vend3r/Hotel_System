package hotelsystem.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.Utils.DatabaseManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GenericDAO<T, ID> {
    void create(T entity) throws DatabaseException, SQLException;
    Optional<T> findById(ID id) throws DatabaseException, SQLException;
    List<T> findAll() throws DatabaseException, SQLException;
    void update(T entity) throws DatabaseException, SQLException;
    void delete(ID id) throws DatabaseException, SQLException;
}