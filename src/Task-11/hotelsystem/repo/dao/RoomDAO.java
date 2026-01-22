package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.Room;
import hotelsystem.enums.RoomCondition;
import hotelsystem.enums.RoomType;

import java.sql.*;
import java.util.Date;

public class RoomDAO extends AbstractDAO<Room, Integer> {

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO rooms (number, type, price, capacity, condition, stars, is_available) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM rooms WHERE number = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE rooms SET type = ?, price = ?, capacity = ?, condition = ?, " +
                "stars = ?, is_available = ? WHERE number = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM rooms WHERE number = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM rooms";
    }

    @Override
    protected void setCreateParameters(PreparedStatement ps, Room room) throws SQLException {
        ps.setInt(1, room.getNumberRoom());
        ps.setString(2, room.getType().name());
        ps.setDouble(3, room.getPriceForDay());
        ps.setInt(4, room.getCapacity());
        ps.setString(5, room.getRoomCondition().name());
        ps.setInt(6, room.getStars());
        ps.setBoolean(7, room.isAvailable());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Room room) throws SQLException {
        ps.setString(1, room.getType().name());
        ps.setDouble(2, room.getPriceForDay());
        ps.setInt(3, room.getCapacity());
        ps.setString(4, room.getRoomCondition().name());
        ps.setInt(5, room.getStars());
        ps.setBoolean(6, room.isAvailable());
        ps.setInt(7, room.getNumberRoom());
    }

    @Override
    protected Room mapResultSetToEntity(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setNumberRoom(rs.getInt("number"));
        room.setType(RoomType.valueOf(rs.getString("type")));
        room.setPriceForDay(rs.getDouble("price"));
        room.setCapacity(rs.getInt("capacity"));
        room.setRoomCondition(RoomCondition.valueOf(rs.getString("condition")));
        room.setStars(rs.getInt("stars"));
        room.setAvailable(rs.getBoolean("is_available"));

        try {
            rs.findColumn("available_date");
            Timestamp availableDate = rs.getTimestamp("available_date");
            if (availableDate != null && !rs.wasNull()) {
                room.setAvailableDate(new Date(availableDate.getTime()));
            }
        } catch (SQLException e) {
            // Колонки нет, ничего не делаем
        }

        return room;
    }
}