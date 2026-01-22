package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.Utils.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDAO<T, ID> implements GenericDAO<T, ID> {
    protected final Connection connection;

    public AbstractDAO() throws DatabaseException {
        try {
            this.connection = DatabaseManager.getInstance().getConnection();
        } catch (DatabaseException e) {
            throw new DatabaseException("Failed to initialize DAO", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getCreateQuery();
    protected abstract String getSelectByIdQuery();
    protected abstract String getUpdateQuery();
    protected abstract String getDeleteQuery();
    protected abstract String getSelectAllQuery();

    protected abstract void setCreateParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract void setUpdateParameters(PreparedStatement ps, T entity) throws SQLException;
    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public void create(T entity) throws DatabaseException {
        try (PreparedStatement ps = connection.prepareStatement(getCreateQuery(), Statement.RETURN_GENERATED_KEYS)) {
            setCreateParameters(ps, entity);
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    setId(entity, generatedKeys.getObject(1));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create entity", e);
        }
    }

    @Override
    public T findById(ID id) throws DatabaseException {
        try (PreparedStatement ps = connection.prepareStatement(getSelectByIdQuery())) {
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapResultSetToEntity(rs) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find entity by ID: " + id, e);
        }
    }

    @Override
    public List<T> findAll() throws DatabaseException {
        List<T> entities = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(getSelectAllQuery());
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                entities.add(mapResultSetToEntity(rs));
            }
            return entities;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find all entities", e);
        }
    }

    @Override
    public void update(T entity) throws DatabaseException {
        try (PreparedStatement ps = connection.prepareStatement(getUpdateQuery())) {
            setUpdateParameters(ps, entity);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update entity", e);
        }
    }

    @Override
    public void delete(ID id) throws DatabaseException {
        try (PreparedStatement ps = connection.prepareStatement(getDeleteQuery())) {
            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete entity with ID: " + id, e);
        }
    }

    protected void setId(T entity, Object id) {
    }
}