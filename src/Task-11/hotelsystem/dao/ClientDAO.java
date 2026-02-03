package hotelsystem.dao;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDAO implements GenericDAO<Client, String> {

    @Override
    public void create(Client client) throws SQLException {
        String sql = "INSERT INTO clients (id, name, surname, room_number) VALUES (?, ?, ?, ?)";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getId());
            ps.setString(2, client.getName());
            ps.setString(3, client.getSurname());
            ps.setInt(4, client.getRoomNumber());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Client> findById(String id) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getString("id"));
                    client.setName(rs.getString("name"));
                    client.setSurname(rs.getString("surname"));
                    client.setRoomNumber(rs.getInt("room_number"));
                    return Optional.of(client);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM clients ORDER BY name, surname";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Client> clients = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Client client = new Client();
                client.setId(rs.getString("id"));
                client.setName(rs.getString("name"));
                client.setSurname(rs.getString("surname"));
                client.setRoomNumber(rs.getInt("room_number"));
                clients.add(client);
            }
            return clients;
        }
    }

    @Override
    public void update(Client client) throws SQLException {
        String sql = "UPDATE clients SET name = ?, surname = ?, room_number = ? WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, client.getName());
            ps.setString(2, client.getSurname());
            if (client.getRoomNumber() > 0) {
                ps.setInt(3, client.getRoomNumber());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            ps.setString(4, client.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM clients WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Client> findByRoomNumber(int roomNumber) throws SQLException {
        String sql = "SELECT * FROM clients WHERE room_number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Client client = new Client();
                    client.setId(rs.getString("id"));
                    client.setName(rs.getString("name"));
                    client.setSurname(rs.getString("surname"));
                    client.setRoomNumber(rs.getInt("room_number"));
                    return Optional.of(client);
                }
                return Optional.empty();
            }
        }
    }
}