package DTO;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PurchaseOrdersDTO{
    enum Status {
        Đang_Chờ,
        Hoàn_Thành,
        Đã_Hủy
    }
    private String id,supplierId;
    private Long employeeId;
    private Status status;
    private float totalAmount;
    private Date buyDate;
    public PurchaseOrdersDTO(String id, String supplierId, Long employeeId, Status status,Date buyDate,float totalAmount) {
        this.id = id;
        this.supplierId = supplierId;
        this.employeeId = employeeId;
        this.status = status;
        this.buyDate = buyDate;
        this.totalAmount = totalAmount;
    }
}
