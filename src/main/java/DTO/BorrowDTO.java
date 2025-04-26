package DTO;

import DTO.Enum.Status;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BorrowDTO
{
    private Long employeeId, id;
    private String readerId;
    private Status status;
    private Date duedate, borrowedDate, actualReturnDate;

    public BorrowDTO() {
    }
    public BorrowDTO(Long id, Long employeeId, String readerId, Date borrowedDate, Date duedate, Status status) {
        this.id = id;
        this.employeeId = employeeId;
        this.readerId = readerId;
        this.borrowedDate = borrowedDate;
        this.duedate = duedate;
        this.status = status;
    }
    public BorrowDTO(Long id, Long employeeId, String readerId, Date borrowedDate, Date duedate, Date actualReturnDate, Status status) {
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

    public Long getLongID(){ return this.id;}
    @Override
    public String toString() {
        return "BorrowDTO{" +
                "employeeId=" + employeeId +
                ", readerId=" + readerId +
                ", id=" + id +
                ", status=" + status +
                ", duedate=" + duedate +
                ", borrowedDate=" + borrowedDate +
                ", actualReturnDate=" + actualReturnDate +
                '}';
    }
}


