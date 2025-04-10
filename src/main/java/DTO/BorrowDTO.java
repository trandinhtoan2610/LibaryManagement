package DTO;

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

    public BorrowDTO(Long id, Long employeeId, Long readerId, Date borrowedDate, Date duedate, Date actualReturnDate, Status status) {
        this.id = id;
        this.employeeId = employeeId;
        this.readerId = readerId;
        this.borrowedDate = borrowedDate;
        this.duedate = duedate;
        this.actualReturnDate = actualReturnDate;
        this.status = status;
    }
    public String getId(){
        return "HD" + String.format("%04d", this.id);
    }
}


