package DTO;

import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderDetail extends EntityBase<Long> {
    private Long purchaseOrderId, bookId;
    private int quantity;
    private float unitPrice, subTotal;
    public PurchaseOrderDetail(Long purchaseOrderId, Long bookId, float unitPrice, int quantity, float subTotal) {
        this.purchaseOrderId = purchaseOrderId;
        this.bookId = bookId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }
}
