package DAL;

import DAL.Interface.IRepositoryBase;
import DAL.Interface.RowMapper;
import DTO.PurchaseOrderDetailDTO;
import DTO.PurchaseOrderDTO; // Import PurchaseOrderDTO
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.math.BigDecimal;

public class PurchaseOrderDetailDAL{
    private final GenericDAL genericDAL = new GenericDAL();
    private final RowMapper<PurchaseOrderDetailDTO> detailRowMapper = (ResultSet rs) -> {
        PurchaseOrderDetailDTO detail = new PurchaseOrderDetailDTO();
        detail.setPurchaseOrderId(rs.getLong("purchaseOrderId"));
        detail.setBookId(rs.getLong("bookId"));
        detail.setQuantity(rs.getInt("quantity"));
        detail.setUnitPrice(rs.getBigDecimal("unitPrice"));
        detail.setSubTotal(rs.getBigDecimal("SubTotal"));
        return detail;
    };

   
    // @Override
    // public PurchaseOrderDetailDTO findById(Long id) {
    //     throw new UnsupportedOperationException("findById không được hỗ trợ cho khóa chính composite.  Sử dụng findByCompositeKey(Long purchaseOrderId, Long bookId)");
    // }

    public PurchaseOrderDetailDTO findByCompositeKey(Long purchaseOrderId, Long bookId) {
        String sql = "SELECT * FROM purchaseordersdetails WHERE purchaseOrderId = ? AND bookId = ?";
        return genericDAL.queryForObject(sql, detailRowMapper, purchaseOrderId, bookId);
    }


    public List<PurchaseOrderDetailDTO> findAll() {
        String sql = "SELECT * FROM purchaseorderdetails";
        return genericDAL.queryForList(sql, detailRowMapper);
    }

    public List<PurchaseOrderDetailDTO> getDetailsByOrderId(Long orderId) {
        String sql = "SELECT * FROM purchaseorderdetails WHERE purchaseOrderId = ?";
        return genericDAL.queryForList(sql, detailRowMapper, orderId);
    }

    public Long create(PurchaseOrderDetailDTO detail) {
        String sql = "INSERT INTO purchaseorderdetails (purchaseOrderId, bookId, quantity, unitPrice, SubTotal) VALUES (?, ?, ?, ?, ?)";
        return genericDAL.insert(sql,
                detail.getPurchaseOrderId(),
                detail.getBookId(),
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubTotal()
        );
    }
    public boolean update(PurchaseOrderDetailDTO detail) {
        String sql = "UPDATE purchaseorderdetails SET quantity = ?, unitPrice = ?, SubTotal = ? WHERE purchaseOrderId = ? AND bookId = ?";
        return genericDAL.update(sql,
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubTotal(),
                detail.getPurchaseOrderId(),
                detail.getBookId()
        );
    }

    // Thêm phương thức update theo Order ID
    public boolean updateByOrderId(PurchaseOrderDetailDTO detail, Long oldOrderId) {
        String sql = "UPDATE purchaseorderdetails SET quantity = ?, unitPrice = ?, SubTotal = ? WHERE purchaseOrderId = ?";
        return genericDAL.update(sql,
                detail.getQuantity(),
                detail.getUnitPrice(),
                detail.getSubTotal(),
                oldOrderId
        );
    }

    
    public boolean delete(Long id) {
        String sql = "DELETE FROM purchaseorderdetails WHERE purchaseOrderId = ?"; // Xóa theo PurchaseOrderID
        return genericDAL.delete(sql, id);
    }


    public boolean deleteByCompositeKey(Long purchaseOrderId, Long bookId) {
        String sql = "DELETE FROM purchaseorderdetails WHERE purchaseOrderId = ? AND bookId = ?";
        return genericDAL.delete(sql, purchaseOrderId, bookId);
    }

//    public BigDecimal getTotalAmountByOrderId(Long orderId) {
//        String sql = "SELECT SUM(SubTotal) AS total FROM purchaseorderdetails WHERE purchaseOrderId = ?";
//        try {
//            ResultSet rs = genericDAL.query(sql, orderId);
//            if (rs.next()) {
//                BigDecimal total = rs.getBigDecimal("total");
//                return total != null ? total : BigDecimal.ZERO;
//            }
//            return BigDecimal.ZERO;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return BigDecimal.ZERO;
//        }
//    }

    public long getCurrentID() {
        String sql = "SELECT MAX(purchaseOrderId) FROM purchaseorderdetails";
        return genericDAL.getMaxID(sql);
    }
}
