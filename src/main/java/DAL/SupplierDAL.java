package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.IRepositoryStringID;
import DAL.Interface.RowMapper;
import DTO.SupplierDTO;

import java.sql.ResultSet;
import java.util.List;

public class SupplierDAL implements IRepositoryStringID<SupplierDTO> {
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<SupplierDTO> supplierRowMapper = (ResultSet rs) -> {
        String id = rs.getString("id");
        String name = rs.getString("name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        return new SupplierDTO(id, name, phone, address);
    };

    @Override
    public SupplierDTO findById(String id) {
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
        String sql = "INSERT INTO Supplier (id, name, phone, address) VALUES (?, ?, ?, ?)"; // Thêm trường id vào câu lệnh INSERT
        return genericDAL.insert(sql, supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getPhone(), supplierDTO.getAddress()); // Thêm supplierDTO.getId() vào tham số
    }

    @Override
    public boolean update(SupplierDTO supplierDTO) {
        String sql = "UPDATE Supplier SET id = ?, name = ?, phone = ?, address = ? WHERE id = ?";
        return genericDAL.update(sql, supplierDTO.getId(), supplierDTO.getName(), supplierDTO.getPhone(), supplierDTO.getAddress(), supplierDTO.getId());
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM Supplier WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() {
        String sql = "SELECT MAX(id) FROM Supplier";
        return genericDAL.getMaxID(sql);
    }
}