package DTO;

import java.math.BigDecimal;

import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
