package DAL;

import DAL.Interface.IRepositoryBase;
import DTO.Employee;
import DTO.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SupplierDAL implements IRepositoryBase<Supplier>{
    private final GenericDAL genericDAL = new GenericDAL();

    private Supplier mapRowToEmployee(ResultSet rs) throws SQLException {
        return new Supplier(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDate("createdAt"),
                rs.getDate("updatedAt")
        );
    }

    @Override
    public Supplier findById(Long id) {
        String sql = "SELECT * FROM supplier WHERE id = ?";
        return genericDAL.queryForObject(sql, this::mapRowToEmployee, id);
    }

    @Override
    public List<Supplier> findAll() {
        return List.of();
    }

    @Override
    public Long create(Supplier supplier) {
        return 0L;
    }

    @Override
    public boolean update(Supplier supplier) {
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
