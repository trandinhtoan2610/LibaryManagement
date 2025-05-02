package DAL;

import DTO.PurchaseOrderDTO;
import DTO.Enum.PurchaseStatus;
import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class PurchaseOrderDAL   {

    private final GenericDAL genericDAL;
    private final RowMapper<PurchaseOrderDTO> purchaseOrderRowMapper = (ResultSet rs) -> {
        PurchaseStatus status = PurchaseStatus.valueOf(rs.getString("status"));
        PurchaseOrderDTO dto = new PurchaseOrderDTO();
        dto.setId(rs.getLong("id"));
        dto.setSupplierId(rs.getString("supplierId"));
        dto.setEmployeeId(rs.getLong("employeeId"));
        dto.setTotalAmount(rs.getDouble("totalAmount"));
        dto.setBuyDate(rs.getTimestamp("buyDate"));
        dto.setStatus(status);
        return dto;
    };

    public PurchaseOrderDAL() {
        this.genericDAL = new GenericDAL();
    }

    
    public PurchaseOrderDTO findById(Long id) throws SQLException {
        String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders WHERE id = ?";
        return genericDAL.queryForObject(sql, purchaseOrderRowMapper, id);
    }

   
    public List<PurchaseOrderDTO> findAll() throws SQLException {
        String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders";
        return genericDAL.queryForList(sql, purchaseOrderRowMapper);
    }

    
    public Long create(PurchaseOrderDTO purchaseOrderDTO) throws SQLException {
        String sql = "INSERT INTO PurchaseOrders (supplierId, employeeId, totalAmount, buyDate, status) VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql, purchaseOrderDTO.getSupplierId(), purchaseOrderDTO.getEmployeeId(), purchaseOrderDTO.getTotalAmount(), purchaseOrderDTO.getBuyDate(), purchaseOrderDTO.getStatus().name());
    }

    public boolean update(PurchaseOrderDTO purchaseOrderDTO) throws SQLException {
        String sql = "UPDATE PurchaseOrders SET supplierId = ?, employeeId = ?, totalAmount = ?, buyDate = ?, status = ? WHERE id = ?";
        return genericDAL.update(sql, purchaseOrderDTO.getSupplierId(), purchaseOrderDTO.getEmployeeId(), purchaseOrderDTO.getTotalAmount(), purchaseOrderDTO.getBuyDate(), purchaseOrderDTO.getStatus().name(), purchaseOrderDTO.getId());
    }

    
    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM PurchaseOrders WHERE id = ?";
        return genericDAL.delete(sql, id);
    }

    public long getCurrentID() throws SQLException{
        String sql = "SELECT MAX(id) from PurchaseOrders";
        return genericDAL.getMaxID(sql);
    }
}
