package DTO;

import DTO.Abstract.EntityBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrderDetail extends EntityBase<Long> {
    private Long purchaseOrderId, bookId, unitPrice, SubTotal;
    private int quantity;
}
