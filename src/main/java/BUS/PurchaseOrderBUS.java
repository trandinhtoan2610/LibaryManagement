package BUS;

import DAL.PurchaseOrderDAL;
import DAL.PurchaseOrderDetailDAL;
import DAL.Interface.IRepositoryBase;
import DTO.PurchaseOrderDTO;
import DTO.PurchaseOrderDetailDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PurchaseOrderBUS {
    private PurchaseOrderDAL purchaseOrderDAL;
    private List<PurchaseOrderDTO> purchaseOrderList;
    private PurchaseOrderDetailDAL purchaseOrderDetailDAL;

    public PurchaseOrderBUS() {
        this.purchaseOrderDAL = new PurchaseOrderDAL();
        this.purchaseOrderDetailDAL = new PurchaseOrderDetailDAL();
        this.purchaseOrderList = new ArrayList<>();
        loadPurchaseOrders();
    }

    // Load tất cả phiếu nhập từ cơ sở dữ liệu
    public void loadPurchaseOrders() {
        try {
            purchaseOrderList.clear();
            List<PurchaseOrderDTO> ordersFromDB = purchaseOrderDAL.findAll();
            if (ordersFromDB != null) {
                purchaseOrderList.addAll(ordersFromDB);
            }
        } catch (SQLException e) {
            Logger.getLogger(PurchaseOrderBUS.class.getName()).log(Level.SEVERE, "Error loading purchase orders: ", e);
        }
    }

    // Lấy tất cả phiếu nhập
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderList;
    }

    // Lấy phiếu nhập theo ID
    public PurchaseOrderDTO getPurchaseOrderById(long id) {
        for (PurchaseOrderDTO order : purchaseOrderList) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

    // Thêm phiếu nhập mới
    public boolean addPurchaseOrder(PurchaseOrderDTO order) {
        try {
            Long newOrderId = purchaseOrderDAL.create(order);
            if (newOrderId != null && newOrderId > 0) {
                order.setId(newOrderId);
                purchaseOrderList.add(order);
                return true;
            }
            return false;
        } catch (SQLException e) {
            Logger.getLogger(PurchaseOrderBUS.class.getName()).log(Level.SEVERE, "Error adding purchase order: ", e);
            return false;
        }
    }

    // Cập nhật phiếu nhập
    public boolean updatePurchaseOrder(PurchaseOrderDTO order) {
        try {
            boolean success = purchaseOrderDAL.update(order);
            if (success) {
                updatePurchaseOrderList(order);
                return true;
            }
            return false;
        } catch (SQLException e) {
            Logger.getLogger(PurchaseOrderBUS.class.getName()).log(Level.SEVERE, "Error updating purchase order: ", e);
            return false;
        }
    }

    // Xóa phiếu nhập
    public boolean deletePurchaseOrder(long id) {
        try {
            boolean success = purchaseOrderDAL.delete(id);
            if (success) {
                purchaseOrderList.removeIf(order -> order.getId() == id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            Logger.getLogger(PurchaseOrderBUS.class.getName()).log(Level.SEVERE, "Error deleting purchase order: ", e);
            return false;
        }
    }

    // Cập nhật danh sách phiếu nhập khi có thay đổi
    private void updatePurchaseOrderList(PurchaseOrderDTO order) {
        for (int i = 0; i < purchaseOrderList.size(); i++) {
            if (purchaseOrderList.get(i).getId() == order.getId()) {
                purchaseOrderList.set(i, order);
                return;
            }
        }
    }
    public Long getCurrentID() throws SQLException {
        return purchaseOrderDAL.getCurrentID();
    }
    public List<PurchaseOrderDetailDTO> getPurchaseOrderDetailsByOrderId(Long orderId) {
        return purchaseOrderDetailDAL.getDetailsByOrderId(orderId);
    }
}
