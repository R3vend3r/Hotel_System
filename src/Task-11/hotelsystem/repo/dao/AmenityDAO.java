package hotelsystem.repo.dao;

import hotelsystem.Exception.DatabaseException;
import hotelsystem.model.Amenity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AmenityDAO extends AbstractDAO<Amenity, String> {

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO amenities (id, name, price) VALUES (?, ?, ?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM amenities WHERE id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE amenities SET name = ?, price = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM amenities WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM amenities";
    }

    @Override
    protected void setCreateParameters(PreparedStatement ps, Amenity amenity) throws SQLException {
        ps.setString(1, amenity.getId());
        ps.setString(2, amenity.getName());
        ps.setDouble(3, amenity.getPrice());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Amenity amenity) throws SQLException {
        ps.setString(1, amenity.getName());
        ps.setDouble(2, amenity.getPrice());
        ps.setString(3, amenity.getId());
    }

    @Override
    protected Amenity mapResultSetToEntity(ResultSet rs) throws SQLException {
        Amenity amenity = new Amenity();
        amenity.setId(rs.getString("id"));
        amenity.setName(rs.getString("name"));
        amenity.setPrice(rs.getDouble("price"));
        return amenity;
    }
}