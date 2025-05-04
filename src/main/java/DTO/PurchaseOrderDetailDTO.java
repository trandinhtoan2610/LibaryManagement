package DTO;

import java.math.BigDecimal;

import DTO.Abstract.EntityBase;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PurchaseOrderDetailDTO{
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
