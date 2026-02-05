package hotelsystem.dao;

import hotelsystem.Utils.DatabaseManager;
import hotelsystem.model.Amenity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AmenityDAO implements GenericDAO<Amenity, String> {

    @Override
    public void create(Amenity amenity) throws SQLException {
        String sql = "INSERT INTO amenities (id, name, price) VALUES (?, ?, ?)";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, amenity.getId());
            ps.setString(2, amenity.getName());
            ps.setDouble(3, amenity.getPrice());
            ps.executeUpdate();
        }
    }

    @Override
    public Optional<Amenity> findById(String id) throws SQLException {
        String sql = "SELECT * FROM amenities WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setId(rs.getString("id"));
                    amenity.setName(rs.getString("name"));
                    amenity.setPrice(rs.getDouble("price"));
                    return Optional.of(amenity);
                }
                return Optional.empty();
            }
        }
    }

    @Override
    public List<Amenity> findAll() throws SQLException {
        String sql = "SELECT * FROM amenities ORDER BY name";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Amenity> amenities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Amenity amenity = new Amenity();
                amenity.setId(rs.getString("id"));
                amenity.setName(rs.getString("name"));
                amenity.setPrice(rs.getDouble("price"));
                amenities.add(amenity);
            }
            return amenities;
        }
    }

    @Override
    public void update(Amenity amenity) throws SQLException {
        String sql = "UPDATE amenities SET name = ?, price = ? WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, amenity.getName());
            ps.setDouble(2, amenity.getPrice());
            ps.setString(3, amenity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String id) throws SQLException {
        String sql = "DELETE FROM amenities WHERE id = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    public Optional<Amenity> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM amenities WHERE name = ?";
        Connection connection = DatabaseManager.getInstance().getConnection();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Amenity amenity = new Amenity();
                    amenity.setId(rs.getString("id"));
                    amenity.setName(rs.getString("name"));
                    amenity.setPrice(rs.getDouble("price"));
                    return Optional.of(amenity);
                }
                return Optional.empty();
            }
        }
    }

    public List<Amenity> findAllSortedByPrice() throws SQLException {
        String sql = "SELECT * FROM amenities ORDER BY price";
        Connection connection = DatabaseManager.getInstance().getConnection();
        List<Amenity> amenities = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Amenity amenity = new Amenity();
                amenity.setId(rs.getString("id"));
                amenity.setName(rs.getString("name"));
                amenity.setPrice(rs.getDouble("price"));
                amenities.add(amenity);
            }
            return amenities;
        }
    }
}