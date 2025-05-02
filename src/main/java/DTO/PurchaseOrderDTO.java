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
    private long id;
    private long employeeId;
    private String supplierId;
    private Status status;
    private Double totalAmount;
    private Date buyDate;
    public PurchaseOrderDTO(long id, String supplierId, long employeeId, Status status,Date buyDate,Double totalAmount) {
        this.id = id;
        this.supplierId = supplierId;
        this.employeeId = employeeId;
        this.status = status;
        this.buyDate = buyDate;
        this.totalAmount = totalAmount;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }

    public long getId() {
        return id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public Status getStatus() {
        return status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public Date getBuyDate() {
        return buyDate;
    }
}
