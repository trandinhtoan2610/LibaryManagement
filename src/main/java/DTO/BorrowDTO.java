package DTO;

import DTO.Abstract.EntityAuditBase;
import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BorrowDTO extends EntityAuditBase<String> {
    private Long employeeId, readerId;
    private Status status;
    private Date duedate, borrowedDate, actualReturnDate;

    public BorrowDTO(String id, Long employeeId, Long readerId, Status status, Date borrowedDate, Date duedate, Date actualReturnDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.readerId = readerId;
        this.status = status;
        this.borrowedDate = borrowedDate;
        this.duedate = duedate;
        this.actualReturnDate = actualReturnDate;
    }
}


