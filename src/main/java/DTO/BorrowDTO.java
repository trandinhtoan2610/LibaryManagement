package DTO;

import DTO.Abstract.EntityAuditBase;
import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BorrowDTO
{
    private Long employeeId, readerId, id;
    private Status status;
    private Date duedate, borrowedDate, actualReturnDate;

    public BorrowDTO(Long id, Long employeeId, Long readerId, Status status, Date borrowedDate, Date duedate, Date actualReturnDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.readerId = readerId;
        this.status = status;
        this.borrowedDate = borrowedDate;
        this.duedate = duedate;
        this.actualReturnDate = actualReturnDate;
    }
    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getReaderId() {
        return readerId;
    }

    public Status getStatus() {
        return status;
    }

    public Date getBorrowedDate() {
        return borrowedDate;
    }

    public Date getDuedate() {
        return duedate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setBorrowedDate(Date borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public void setDuedate(Date duedate) {
        this.duedate = duedate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
    public String getId(){
        return "HD" + String.format("%04d", this.id);
    }
    
    
}


