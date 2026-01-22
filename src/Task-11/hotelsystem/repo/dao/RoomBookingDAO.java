package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.Client;
import hotelsystem.model.Room;
import hotelsystem.model.RoomBooking;

import java.sql.*;
import java.util.Date;

public class RoomBookingDAO extends AbstractDAO<RoomBooking, String> {

    private RoomDAO roomDAO;

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO room_bookings (id, client_id, room_number, check_in_date, check_out_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM room_bookings WHERE id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE room_bookings SET client_id = ?, room_number = ?, check_in_date = ?, " +
                "check_out_date = ?, total_price = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM room_bookings WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM room_bookings";
    }

    @Override
    protected void setCreateParameters(PreparedStatement ps, RoomBooking booking) throws SQLException {
        ps.setString(1, booking.getId());
        ps.setString(2, booking.getClientId());
        ps.setInt(3, booking.getRoom().getNumberRoom());
        ps.setTimestamp(4, new Timestamp(booking.getCheckInDate().getTime()));
        ps.setTimestamp(5, new Timestamp(booking.getCheckOutDate().getTime()));
        ps.setDouble(6, booking.getTotalPrice());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, RoomBooking booking) throws SQLException {
        ps.setString(1, booking.getClientId());
        ps.setInt(2, booking.getRoom().getNumberRoom());
        ps.setTimestamp(3, new Timestamp(booking.getCheckInDate().getTime()));
        ps.setTimestamp(4, new Timestamp(booking.getCheckOutDate().getTime()));
        ps.setDouble(5, booking.getTotalPrice());
        ps.setString(6, booking.getId());
    }

    @Override
    protected RoomBooking mapResultSetToEntity(ResultSet rs) throws SQLException {
        try {
            Room room = roomDAO.findById(rs.getInt("room_number"));
            return new RoomBooking(
                    rs.getString("id"),
                    null,
                    room,
                    rs.getDouble("total_price"),
                    new Date(rs.getTimestamp("check_in_date").getTime()),
                    new Date(rs.getTimestamp("check_out_date").getTime())
            );
        } catch (DatabaseException e) {
            throw new SQLException("Failed to load related entities", e);
        }
    }
}