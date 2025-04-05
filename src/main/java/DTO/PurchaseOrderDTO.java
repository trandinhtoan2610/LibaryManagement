package DTO;

import DTO.Abstract.EntityAuditBase;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseOrderDTO{
    enum Status {
        Đang_Chờ,
        Hoàn_Thành,
        Đã_Hủy
    }
    private long id,supplierId, employeeId;
    private Status status;
    private float totalAmount;
    private Date buyDate;
    public PurchaseOrderDTO(Long id, long supplierId, long employeeId, Status status,Date buyDate,float totalAmount) {
        this.id = id;
        this.supplierId = supplierId;
        this.employeeId = employeeId;
        this.status = status;
        this.buyDate = buyDate;
        this.totalAmount = totalAmount;
    }
}
