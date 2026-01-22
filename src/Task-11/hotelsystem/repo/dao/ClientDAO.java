package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDAO extends AbstractDAO<Client, String> {

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO clients (id, name, surname, room_number) VALUES (?, ?, ?, ?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM clients WHERE id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE clients SET name = ?, surname = ?, room_number = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM clients WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM clients";
    }

    @Override
    protected void setCreateParameters(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getId());
        ps.setString(2, client.getName());
        ps.setString(3, client.getSurname());
        ps.setInt(4, client.getRoomNumber());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Client client) throws SQLException {
        ps.setString(1, client.getName());
        ps.setString(2, client.getSurname());
        ps.setInt(3, client.getRoomNumber());
        ps.setString(4, client.getId());
    }

    @Override
    protected Client mapResultSetToEntity(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getString("id"));
        client.setName(rs.getString("name"));
        client.setSurname(rs.getString("surname"));
        client.setRoomNumber(rs.getInt("room_number"));
        return client;
    }
}