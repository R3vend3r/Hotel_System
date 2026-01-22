package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;

import java.util.List;

public interface GenericDAO<T, ID> {
    void create(T entity) throws DatabaseException;
    T findById(ID id) throws DatabaseException;
    List<T> findAll() throws DatabaseException;
    void update(T entity) throws DatabaseException;
    void delete(ID id) throws DatabaseException;
}