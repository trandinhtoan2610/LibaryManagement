package DTO;

import DTO.Abstract.EntityAuditBase;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseOrder extends EntityAuditBase<Long> {
    enum Status {
        Đang_Chờ,
        Hoàn_Thành,
        Đã_Hủy
    }
    private long supplierId, employeeId;
    private Status status;
    private Long totalAmount;
}
