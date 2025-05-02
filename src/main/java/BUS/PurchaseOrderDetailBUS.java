package BUS;

import DAL.PurchaseOrderDetailDAL;
import DTO.Book;
import DTO.PurchaseOrderDetailDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PurchaseOrderDetailBUS {
    private PurchaseOrderDetailDAL purchaseOrderDetailDAL = new PurchaseOrderDetailDAL();
    public static List<PurchaseOrderDetailDTO> purchaseOrderDetailList;
    private final BookBUS bookBUS = new BookBUS();

    public PurchaseOrderDetailBUS() {
        if (purchaseOrderDetailList == null) {
            getPurchaseOrderDetailList();
        }
    }

    public void getPurchaseOrderDetailList() {
        try {
            purchaseOrderDetailList = purchaseOrderDetailDAL.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy danh sách chi tiết phiếu nhập!", e);
        }
    }

    public void addPurchaseOrderDetail(Long purchaseOrderId, Long bookId, int quantity) {
        try {
            Book book = bookBUS.getBookById(bookId);
            double unitPrice = book.getUnitPrice();
            double subTotal = unitPrice * quantity;

            PurchaseOrderDetailDTO detail = new PurchaseOrderDetailDTO(
                    purchaseOrderId,
                    bookId,
                    quantity,
                    unitPrice,
                    subTotal
            );

            purchaseOrderDetailDAL.create(detail);
            purchaseOrderDetailList.add(detail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi thêm chi tiết phiếu nhập!");
        }
    }

    public void deletePurchaseOrderDetailsByOrderId(Long purchaseOrderId) {
        try {
            Iterator<PurchaseOrderDetailDTO> iterator = purchaseOrderDetailList.iterator();
            while (iterator.hasNext()) {
                PurchaseOrderDetailDTO detail = iterator.next();
                if (detail.getPurchaseOrderId().equals(purchaseOrderId)) {
                    iterator.remove();
                    purchaseOrderDetailDAL.delete(detail);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi xóa chi tiết phiếu nhập!", e);
        }
    }

    public List<PurchaseOrderDetailDTO> getDetailsByOrderId(Long purchaseOrderId) {
        return purchaseOrderDetailDAL.findByOrderId(purchaseOrderId);
    }

    public double getTotalAmount(Long purchaseOrderId) {
        double total = 0;
        for (PurchaseOrderDetailDTO detail : purchaseOrderDetailList) {
            if (detail.getPurchaseOrderId().equals(purchaseOrderId)) {
                total += detail.getSubTotal();
            }
        }
        return total;
    }
}
