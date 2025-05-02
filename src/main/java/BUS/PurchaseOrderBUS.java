package BUS;

import DAL.PurchaseOrderDAL;
import DAL.Interface.IRepositoryBase;
import DTO.BorrowDTO;
import DTO.PurchaseOrderDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PurchaseOrderBUS{
    private PurchaseOrderDAL purchaseOrderDAL;
    private static List<PurchaseOrderDTO> purchaseOrderList = new ArrayList<>();

    public PurchaseOrderBUS() {
        this.purchaseOrderDAL = new PurchaseOrderDAL();
        if (purchaseOrderList.isEmpty()) {
            loadPurchaseOrders();
        }
    }

    public void loadPurchaseOrders() {
        try {
            purchaseOrderList.clear();
            List<PurchaseOrderDTO> ordersFromDB = purchaseOrderDAL.findAll();
            if (ordersFromDB != null) {
                purchaseOrderList.addAll(ordersFromDB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderList;
    }

    public PurchaseOrderDTO getPurchaseOrderById(long id) {
        for (PurchaseOrderDTO order : purchaseOrderList) {
            if (order.getId() == id) {
                return order;
            }
        }
        return null;
    }

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
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePurchaseOrder(PurchaseOrderDTO order) {
        try {
            boolean success = purchaseOrderDAL.update(order);
            if (success) {
                for (int i = 0; i < purchaseOrderList.size(); i++) {
                    if (purchaseOrderList.get(i).getId() == order.getId()) {
                        purchaseOrderList.set(i, order);
                        return true;
                    }
                }
               
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePurchaseOrder(long id) {
        try {
            boolean success = purchaseOrderDAL.delete(id);
            if (success) {
                purchaseOrderList.removeIf(order -> order.getId() == id);
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean validatePurchaseOrder(PurchaseOrderDTO order) {
        return order.getSupplierId() != null && !order.getSupplierId().isEmpty()
                && order.getEmployeeId() > 0
                && order.getTotalAmount() >= 0;
    }
}
