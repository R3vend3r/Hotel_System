package hotelsystem.repo.dao;

import hotelsystem.model.AmenityOrder;
import java.sql.*;

public class AmenityOrderDAO extends AbstractDAO<AmenityOrder, String> {

    @Override
    protected String getCreateQuery() {
        return "INSERT INTO amenity_orders (id, client_id, amenity_id, service_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String getSelectByIdQuery() {
        return "SELECT * FROM amenity_orders WHERE id = ?";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE amenity_orders SET client_id = ?, amenity_id = ?, service_date = ?, " +
                "total_price = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM amenity_orders WHERE id = ?";
    }

    @Override
    protected String getSelectAllQuery() {
        return "SELECT * FROM amenity_orders";
    }

    @Override
    protected void setCreateParameters(PreparedStatement ps, AmenityOrder order) throws SQLException {
        ps.setString(1, order.getId());
        ps.setString(2, order.getClientId());
        ps.setString(3, order.getAmenityId());
        ps.setTimestamp(4, new Timestamp(order.getServiceDate().getTime()));
        ps.setDouble(5, order.getTotalPrice());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, AmenityOrder order) throws SQLException {
        ps.setString(1, order.getClientId());
        ps.setString(2, order.getAmenityId());
        ps.setTimestamp(3, new Timestamp(order.getServiceDate().getTime()));
        ps.setDouble(4, order.getTotalPrice());
        ps.setString(5, order.getId());
    }

    @Override
    protected AmenityOrder mapResultSetToEntity(ResultSet rs) throws SQLException {
        return new AmenityOrder(
                rs.getString("id"),
                rs.getString("client_id"),
                rs.getDouble("total_price"),
                rs.getString("amenity_id"),
                new Date(rs.getTimestamp("service_date").getTime())
        );
    }
}