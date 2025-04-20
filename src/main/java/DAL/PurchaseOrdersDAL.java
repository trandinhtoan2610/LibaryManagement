// package DAL;

// import DAL.Interface.IRepositoryBase;
// import DAL.Interface.RowMapper;
// import DTO.PurchaseOrdersDTO;

// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Timestamp;
// import java.util.List;

// public class PurchaseOrdersDAL implements IRepositoryBase<PurchaseOrdersDTO> {
//     private final GenericDAL genericDAL;
//     private final RowMapper<PurchaseOrdersDTO> purchaseOrdersRowMapper = (ResultSet rs) -> {
//         try {
//             String id = rs.getString("id");
//             String supplierId = rs.getString("supplierId");
//             Long employeeId = rs.getLong("employeeId");
//             Double totalAmount = rs.getDouble("totalAmount");
//             Timestamp buyDate = rs.getTimestamp("buyDate");
//             String status = rs.getString("status");
//             return new PurchaseOrdersDTO(id, supplierId, employeeId, totalAmount, buyDate, status);
//         } catch (SQLException e) {
//             System.err.println("Error mapping PurchaseOrders from ResultSet: " + e.getMessage());
//             return null;
//         }
//     };

//     public PurchaseOrdersDAL() {
//         this.genericDAL = new GenericDAL();
//     }

//     @Override
//     public PurchaseOrdersDTO findById(Long id) {
//         String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders WHERE id = ?";
//         return genericDAL.queryForObject(sql, purchaseOrdersRowMapper, String.valueOf(id));
//     }

//     public PurchaseOrdersDTO findByPurchaseOrderId(String id) {
//         String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders WHERE id = ?";
//         return genericDAL.queryForObject(sql, purchaseOrdersRowMapper, id);
//     }

//     @Override
//     public List<PurchaseOrdersDTO> findAll() {
//         String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders";
//         return genericDAL.queryForList(sql, purchaseOrdersRowMapper);
//     }

//     @Override
//     public Long create(PurchaseOrdersDTO purchaseOrdersDTO) {
//         String sql = "INSERT INTO PurchaseOrders (id, supplierId, employeeId, totalAmount, buyDate, status) VALUES (?, ?, ?, ?, ?, ?)";
//         return genericDAL.insert(sql, purchaseOrdersDTO.getId(), purchaseOrdersDTO.getSupplierId(), purchaseOrdersDTO.getEmployeeId(), purchaseOrdersDTO.getTotalAmount(), purchaseOrdersDTO.getBuyDate(), purchaseOrdersDTO.getStatus());
//     }

//     @Override
//     public boolean update(PurchaseOrdersDTO purchaseOrdersDTO) {
//         String sql = "UPDATE PurchaseOrders SET supplierId = ?, employeeId = ?, totalAmount = ?, buyDate = ?, status = ? WHERE id = ?";
//         return genericDAL.update(sql, purchaseOrdersDTO.getSupplierId(), purchaseOrdersDTO.getEmployeeId(), purchaseOrdersDTO.getTotalAmount(), purchaseOrdersDTO.getBuyDate(), purchaseOrdersDTO.getStatus(), purchaseOrdersDTO.getId());
//     }

//     @Override
//     public boolean delete(Long id) {
//         String sql = "DELETE FROM PurchaseOrders WHERE id = ?";
//         return genericDAL.delete(sql, String.valueOf(id));
//     }

//     public boolean deleteByPurchaseOrderId(String id) {
//         String sql = "DELETE FROM PurchaseOrders WHERE id = ?";
//         return genericDAL.delete(sql, id);
//     }

//     public long getCurrentID() {
//         String sql = "SELECT MAX(CAST(SUBSTR(id, 3) AS UNSIGNED)) FROM PurchaseOrders WHERE id LIKE 'PO%'";
//         return genericDAL.getMaxID(sql);
//     }

//     // Các phương thức khác có thể cần thiết
//     // Ví dụ: tìm kiếm theo trạng thái, theo khoảng thời gian, v.v.

//     /**
//      * Tìm kiếm các đơn đặt hàng theo trạng thái.
//      *
//      * @param status Trạng thái cần tìm kiếm (ví dụ: 'Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy').
//      * @return Danh sách các PurchaseOrdersDTO có trạng thái tương ứng.
//      */
//     public List<PurchaseOrdersDTO> findByStatus(String status) {
//         String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders WHERE status = ?";
//         return genericDAL.queryForList(sql, purchaseOrdersRowMapper, status);
//     }

//     /**
//      * Tìm kiếm các đơn đặt hàng được tạo trong một khoảng thời gian nhất định.
//      *
//      * @param startDate Thời gian bắt đầu của khoảng (bao gồm).
//      * @param endDate   Thời gian kết thúc của khoảng (bao gồm).
//      * @return Danh sách các PurchaseOrdersDTO được tạo trong khoảng thời gian này.
//      */
//     public List<PurchaseOrdersDTO> findByDateRange(Timestamp startDate, Timestamp endDate) {
//         String sql = "SELECT id, supplierId, employeeId, totalAmount, buyDate, status FROM PurchaseOrders WHERE buyDate >= ? AND buyDate <= ?";
//         return genericDAL.queryForList(sql, purchaseOrdersRowMapper, startDate, endDate);
//     }

//     /**
//      * Cập nhật trạng thái của một đơn đặt hàng cụ thể.
//      *
//      * @param purchaseOrderId ID của đơn đặt hàng cần cập nhật.
//      * @param newStatus       Trạng thái mới ('Đang_Chờ', 'Hoàn_Thành', 'Đã_Hủy').
//      * @return true nếu cập nhật thành công, false nếu không.
//      */
//     public boolean updateStatus(String purchaseOrderId, String newStatus) {
//         String sql = "UPDATE PurchaseOrders SET status = ? WHERE id = ?";
//         return genericDAL.update(sql, newStatus, purchaseOrderId);
//     }

//     /**
//      * Cập nhật tổng số tiền của một đơn đặt hàng.
//      *
//      * @param purchaseOrderId ID của đơn đặt hàng cần cập nhật.
//      * @param newTotalAmount  Tổng số tiền mới.
//      * @return true nếu cập nhật thành công, false nếu không.
//      */
//     public boolean updateTotalAmount(String purchaseOrderId, Double newTotalAmount) {
//         String sql = "UPDATE PurchaseOrders SET totalAmount = ? WHERE id = ?";
//         return genericDAL.update(sql, newTotalAmount, purchaseOrderId);
//     }
// }