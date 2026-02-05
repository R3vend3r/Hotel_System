package hotelsystem.dao;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.model.Room;
import hotelsystem.enums.RoomCondition;
import hotelsystem.enums.RoomType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomDAO implements GenericDAO<Room, Integer> {

    @Override
    public void create(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (number, type, price, capacity, condition, stars, is_available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, room.getNumberRoom());
            ps.setString(2, room.getType().name());
            ps.setDouble(3, room.getPriceForDay());
            ps.setInt(4, room.getCapacity());
            ps.setString(5, room.getRoomCondition().name());
            ps.setInt(6, room.getStars());
            ps.setBoolean(7, room.isAvailable());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Room> findById(Integer number) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToRoom(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Room> findAll() throws SQLException {
        String sql = "SELECT * FROM rooms ORDER BY number";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Room> rooms = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
        }
    }

    @Override
    public void update(Room room) throws SQLException {
        String sql = "UPDATE rooms SET type = ?, price = ?, capacity = ?, condition = ?, " +
                "stars = ?, is_available = ? WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, room.getType().name());
            ps.setDouble(2, room.getPriceForDay());
            ps.setInt(3, room.getCapacity());
            ps.setString(4, room.getRoomCondition().name());
            ps.setInt(5, room.getStars());
            ps.setBoolean(6, room.isAvailable());
            ps.setInt(7, room.getNumberRoom());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer number) throws SQLException {
        String sql = "DELETE FROM rooms WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, number);
            ps.executeUpdate();
        }
    }

    public List<Room> findAvailableRooms() throws SQLException {
        String sql = "SELECT * FROM rooms WHERE is_available = true ORDER BY number";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Room> rooms = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rooms.add(mapResultSetToRoom(rs));
            }
            return rooms;
        }
    }

    public boolean isRoomAvailable(int number) throws SQLException {
        String sql = "SELECT is_available FROM rooms WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, number);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_available");
                }
                return false;
            }
        }
    }

    public void updateRoomStatus(int number, RoomCondition status) throws SQLException {
        String sql = "UPDATE rooms SET condition = ? WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt(2, number);
            ps.executeUpdate();
        }
    }

    public void updateRoomPrice(int number, double newPrice) throws SQLException {
        String sql = "UPDATE rooms SET price = ? WHERE number = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, newPrice);
            ps.setInt(2, number);
            ps.executeUpdate();
        }
    }

    public int countAvailableRooms() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM rooms WHERE is_available = true";
        Connection connection = DatabaseManager.getInstance().getConnection();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("count");
            }
            return 0;
        }
    }

    private Room mapResultSetToRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setNumberRoom(rs.getInt("number"));
        room.setType(RoomType.valueOf(rs.getString("type")));
        room.setPriceForDay(rs.getDouble("price"));
        room.setCapacity(rs.getInt("capacity"));
        room.setRoomCondition(RoomCondition.valueOf(rs.getString("condition")));
        room.setStars(rs.getInt("stars"));
        room.setAvailable(rs.getBoolean("is_available"));
        return room;
    }
}