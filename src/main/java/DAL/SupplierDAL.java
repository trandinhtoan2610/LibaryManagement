package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierDAL implements IRepositoryBase<Supplier>{
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<Supplier> SupplierRowMapper = this::mapRowToSupplier;
    private Supplier mapRowToSupplier(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("address"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }

    @Override
    public Supplier findById(Long id) {
        String sql = "SELECT * FROM supplier WHERE id = ?";
        return genericDAL.queryForObject(sql, SupplierRowMapper, id);
    }

    @Override
    public List<Supplier> findAll() {
        String sql = "SELECT * FROM supplier";
        return genericDAL.queryForList(sql,SupplierRowMapper);
    }

    @Override
    public Long create(Supplier supplier) {
        String sql = "INSERT INTO supplier (name, phone, address) VALUES (?, ?, ?)";
        return genericDAL.insert(sql, supplier.getName());
    }

    @Override
    public boolean update(Supplier supplier) {
        String sql = "UPDATE supplier SET name = ?, SET phone = ?, SET address = ? WHERE id = ?";
        return genericDAL.update(sql, supplier.getName(), supplier.getPhone(), supplier.getAddress(), supplier.getId());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM supplier WHERE id = ?";
        return genericDAL.delete(sql, id);
    }
}
