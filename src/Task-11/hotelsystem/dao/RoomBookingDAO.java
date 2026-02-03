package hotelsystem.dao;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.model.RoomBooking;

import java.util.Date;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomBookingDAO implements GenericDAO<RoomBooking, String> {

    @Override
    public void create(RoomBooking booking) throws SQLException {
        String sql = "INSERT INTO room_bookings (id, client_id, room_number, check_in_date, check_out_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, booking.getId());
            ps.setString(2, booking.getClientId());
            ps.setInt(3, booking.getRoom().getNumberRoom());
            ps.setTimestamp(4, new Timestamp(booking.getCheckInDate().getTime()));
            ps.setTimestamp(5, new Timestamp(booking.getCheckOutDate().getTime()));
            ps.setDouble(6, booking.getTotalPrice());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<RoomBooking> findById(String id) throws SQLException {
        String sql = "SELECT * FROM room_bookings WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<RoomBooking> findAll() throws SQLException {
        String sql = "SELECT * FROM room_bookings ORDER BY check_in_date DESC";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<RoomBooking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
        }
    }

    @Override
    public void update(RoomBooking booking) throws SQLException {
        String sql = "UPDATE room_bookings SET client_id = ?, room_number = ?, check_in_date = ?, " +
                "check_out_date = ?, total_price = ? WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, booking.getClientId());
            ps.setInt(2, booking.getRoomNumber());
            ps.setTimestamp(3, new Timestamp(booking.getCheckInDate().getTime()));
            ps.setTimestamp(4, new Timestamp(booking.getCheckOutDate().getTime()));
            ps.setDouble(5, booking.getTotalPrice());
            ps.setString(6, booking.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM room_bookings WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<RoomBooking> findActiveByRoom(int roomNumber) throws SQLException {
        String sql = """
            SELECT * FROM room_bookings 
            WHERE room_number = ? 
            AND check_out_date > CURRENT_TIMESTAMP
            ORDER BY check_in_date DESC 
            LIMIT 1
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBooking(rs));
                }
                return Optional.empty();
            }
        }
    }

    public List<RoomBooking> findActiveBookings() throws SQLException {
        String sql = """
            SELECT * FROM room_bookings 
            WHERE check_out_date > CURRENT_TIMESTAMP
            ORDER BY check_in_date DESC
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<RoomBooking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
        }
    }

    public List<RoomBooking> findCompletedBookings() throws SQLException {
        String sql = """
               SELECT rb.*, c.name, c.surname 
               FROM room_bookings rb
               LEFT JOIN clients c ON rb.client_id = c.id
               WHERE rb.check_out_date <= CURRENT_TIMESTAMP
               ORDER BY rb.check_out_date DESC
               """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<RoomBooking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
            return bookings;
        }
    }

    public List<RoomBooking> findByRoom(int roomNumber, int limit) throws SQLException {
        String sql = """
            SELECT rb.*, c.name, c.surname 
            FROM room_bookings rb
            LEFT JOIN clients c ON rb.client_id = c.id
            WHERE rb.room_number = ? 
            ORDER BY rb.check_out_date DESC 
            LIMIT ?
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<RoomBooking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            ps.setInt(2, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            return bookings;
        }
    }

    public List<RoomBooking> findAllByRoom(int roomNumber) throws SQLException {
        String sql = """
            SELECT rb.*, c.name, c.surname 
            FROM room_bookings rb
            LEFT JOIN clients c ON rb.client_id = c.id
            WHERE rb.room_number = ? 
            ORDER BY rb.check_out_date DESC
            """;
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<RoomBooking> bookings = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
            return bookings;
        }
    }

    public double calculateTotalIncome() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_price), 0) as total FROM room_bookings";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("total");
            }
            return 0.0;
        }
    }

    public double calculateStayCost(int roomNumber, Date endDate) throws SQLException {
        Optional<RoomBooking> bookingOpt = findActiveByRoom(roomNumber);
        if (bookingOpt.isEmpty()) {
            return 0.0;
        }

        RoomBooking booking = bookingOpt.get();
        Date startDate = booking.getCheckInDate();

        long days = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24);
        return booking.getTotalPrice() / Math.max(1, days) * days;
    }

    private RoomBooking mapResultSetToBooking(ResultSet rs) throws SQLException {
        RoomBooking booking = new RoomBooking();
        booking.setId(rs.getString("id"));
        booking.setClientId(rs.getString("client_id"));
        booking.setRoomNumber(rs.getInt("room_number"));
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setCheckInDate(new java.util.Date(rs.getTimestamp("check_in_date").getTime()));

        Timestamp checkOutTimestamp = rs.getTimestamp("check_out_date");
        if (checkOutTimestamp != null) {
            booking.setCheckOutDate(new java.util.Date(checkOutTimestamp.getTime()));
        }

        try {
            String firstName = rs.getString("name");
            String lastName = rs.getString("surname");
            if (firstName != null && lastName != null) {
                booking.setClientInfo(firstName + " " + lastName);
            } else {
                booking.setClientInfo("ID: " + rs.getString("client_id"));
            }
        } catch (SQLException e) {
            booking.setClientInfo("ID: " + rs.getString("client_id"));
        }

        return booking;
    }
}