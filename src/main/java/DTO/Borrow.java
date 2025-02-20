package DTO;

import DTO.Abstract.EntityAuditBase;

import java.util.Date;


public class Borrow extends EntityAuditBase<Long> {
    private Long employeeId,customerId;
    private Status status;
    private Date duedate;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDuedate() {
        return duedate;
    }

    public enum Status{
        BORROW,
        RETURNED,
        OVERDUE
    }
}


