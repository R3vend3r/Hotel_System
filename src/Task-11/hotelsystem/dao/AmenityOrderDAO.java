package hotelsystem.dao;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.model.AmenityOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityOrderDAO implements GenericDAO<AmenityOrder, String> {

    @Override
    public void create(AmenityOrder order) throws SQLException {
        String sql = "INSERT INTO amenity_orders (id, client_id, amenity_id, service_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?)";
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getId());
            ps.setString(2, order.getClientId());
            ps.setString(3, order.getAmenityId());
            ps.setTimestamp(4, new Timestamp(order.getServiceDate().getTime()));
            ps.setDouble(5, order.getTotalPrice());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<AmenityOrder> findById(String id) throws SQLException {
        String sql = "SELECT * FROM amenity_orders WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToOrder(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<AmenityOrder> findAll() throws SQLException {
        String sql = "SELECT * FROM amenity_orders ORDER BY service_date DESC";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<AmenityOrder> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            return orders;
        }
    }

    @Override
    public void update(AmenityOrder order) throws SQLException {
        String sql = "UPDATE amenity_orders SET client_id = ?, amenity_id = ?, service_date = ?, " +
                "total_price = ? WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, order.getClientId());
            ps.setString(2, order.getAmenityId());
            ps.setTimestamp(3, new Timestamp(order.getServiceDate().getTime()));
            ps.setDouble(4, order.getTotalPrice());
            ps.setString(5, order.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM amenity_orders WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public List<AmenityOrder> findByClientId(String clientId) throws SQLException {
        String sql = "SELECT * FROM amenity_orders WHERE client_id = ? ORDER BY service_date DESC";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<AmenityOrder> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, clientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            return orders;
        }
    }

    public List<AmenityOrder> findByRoomNumber(int roomNumber) throws SQLException {
        String sql = """
            SELECT ao.* FROM amenity_orders ao
            JOIN clients c ON ao.client_id = c.id
            WHERE c.room_number = ?
            ORDER BY ao.service_date DESC
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<AmenityOrder> orders = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(mapResultSetToOrder(rs));
                }
            }
            return orders;
        }
    }

    public double calculateTotalForRoom(int roomNumber) throws SQLException {
        String sql = """
            SELECT COALESCE(SUM(ao.total_price), 0) as total 
            FROM amenity_orders ao
            JOIN clients c ON ao.client_id = c.id
            WHERE c.room_number = ?
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("total");
                }
                return 0.0;
            }
        }
    }

    public double calculateTotalIncome() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_price), 0) as total FROM amenity_orders";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;
        }
    }

    private AmenityOrder mapResultSetToOrder(ResultSet rs) throws SQLException {
        return new AmenityOrder(
                rs.getString("id"),
                rs.getString("client_id"),
                rs.getDouble("total_price"),
                rs.getString("amenity_id"),
                new java.util.Date(rs.getTimestamp("service_date").getTime())
        );
    }
}