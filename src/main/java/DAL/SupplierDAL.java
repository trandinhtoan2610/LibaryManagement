package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.SupplierDTO;

import java.sql.ResultSet;
import java.util.List;

public class SupplierDAL implements IRepositoryBase<SupplierDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<SupplierDTO> supplierRowMapper = (ResultSet rs) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name"); 
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        return new SupplierDTO(id, name, phone, address);
    };

    @Override
    public SupplierDTO findById(Long id) {
        String sql = "SELECT * FROM Supplier WHERE id = ?";
        return genericDAL.queryForObject(sql, supplierRowMapper, id);
    }

    @Override
    public List<SupplierDTO> findAll() {
        String sql = "SELECT * FROM Supplier";
        return genericDAL.queryForList(sql, supplierRowMapper);
    }

    @Override
    public Long create(SupplierDTO supplierDTO) {
        String sql = "INSERT INTO Supplier (name, phone, address) VALUES (?, ?, ?)"; 
        return genericDAL.insert(sql, supplierDTO.getName(), supplierDTO.getPhone(), supplierDTO.getAddress());
    }

    @Override
    public boolean update(SupplierDTO supplierDTO) {
        String sql = "UPDATE Supplier SET name = ?, phone = ?, address = ? WHERE id = ?"; 
        return genericDAL.update(sql, supplierDTO.getName(), supplierDTO.getPhone(), supplierDTO.getAddress());
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM Supplier WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM Supplier";
        return genericDAL.getMaxID(sql);
    }
}