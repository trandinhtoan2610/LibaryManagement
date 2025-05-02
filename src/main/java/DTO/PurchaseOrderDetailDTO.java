package DTO;

import java.math.BigDecimal;

import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderDetailDTO extends EntityBase<Long> {
    private Long purchaseOrderId, bookId;
    private int quantity;
    private BigDecimal unitPrice, subTotal;
    
    public PurchaseOrderDetailDTO(Long purchaseOrderId, Long bookId, BigDecimal unitPrice, int quantity, BigDecimal subTotal) {
        this.purchaseOrderId = purchaseOrderId;
        this.bookId = bookId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subTotal = subTotal;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public Long getBookId() {
        return bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public Long getId() {
        return id;
    }

    public void setPurchaseOrderId(Long purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
